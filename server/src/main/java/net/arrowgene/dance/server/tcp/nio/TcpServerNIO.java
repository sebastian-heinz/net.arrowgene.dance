/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
 * Copyright (C) 2013-2018  Daniel Neuendorf
 *
 * Github: https://github.com/Arrowgene/net.arrowgene.dance
 * Web: https://arrowgene.net
 *
 * net.arrowgene.dance is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * net.arrowgene.dance is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.arrowgene.dance.server.tcp.nio;


import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerConfig;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.tcp.TcpClient;
import net.arrowgene.dance.server.tcp.TcpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class TcpServerNIO extends TcpServer implements Runnable {

    private final Object pendingChangesLock = new Object();
    private final Object pendingDataLock = new Object();
    private volatile boolean isRunning;

    private Selector selector;
    private PacketWorker worker;
    private LinkedList<ChangeRequest> pendingChanges;
    private HashMap<SocketChannel, ArrayList<ByteBuffer>> pendingData;
    private ServerConfig serverConfig;
    private ByteBuffer readBuffer;
    private Thread tcpThread;
    private ServerSocketChannel serverChannel;

    public TcpServerNIO(DanceServer server) {
        super(server);
        this.pendingChanges = new LinkedList<>();
        this.pendingData = new HashMap<>();
        this.serverConfig = server.getServerConfig();
        this.worker = new PacketWorker(this, super.getLogger());
        this.readBuffer = ByteBuffer.allocate(8192);
    }

    @Override
    public void start() {
        if (!this.isRunning) {
            super.port = this.serverConfig.getPort();
            super.hostAddress = this.serverConfig.getHostAddress();
            try {
                this.selector = this.initSelector();
                this.worker.start();
                this.tcpThread = new Thread(this);
                this.tcpThread.setName("TcpServerNIO");
                this.tcpThread.start();
            } catch (IOException e) {
                super.getLogger().writeLog(e);
            }
        } else {
            super.getLogger().writeLog(LogType.WARNING, "TcpServerNIO", "start", "Server already started");
        }
    }

    @Override
    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            this.worker.stop();
            try {
                this.serverChannel.close();
                this.selector.close();
            } catch (IOException e) {
                super.getLogger().writeLog(e);
            }
        } else {
            super.getLogger().writeLog(LogType.WARNING, "TcpServerNIO", "stop", "Server already stopped");
        }
    }

    @Override
    public void writeDebugInfo() {
        synchronized (this.pendingChangesLock) {
            super.getLogger().writeLog(LogType.DEBUG, "TcpServerNIO", "writeDebugInfo", "Pending Changes: " + this.pendingChanges.size());
        }
        synchronized (this.pendingDataLock) {
            super.getLogger().writeLog(LogType.DEBUG, "TcpServerNIO", "writeDebugInfo", "Pending Data: " + this.pendingData.size());
        }
        this.worker.writeDebugInfo();
    }

    public void send(SocketChannel socket, byte[] data) {
        synchronized (this.pendingChangesLock) {
            // Indicate we want the interest ops set changed
            this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

            // And queue the data we want written
            synchronized (this.pendingDataLock) {
                ArrayList<ByteBuffer> queue = this.pendingData.get(socket);
                if (queue == null) {
                    queue = new ArrayList<ByteBuffer>();
                    this.pendingData.put(socket, queue);
                }
                queue.add(ByteBuffer.wrap(data));
            }
        }

        // Finally, wake up our selecting thread so it can make the required changes
        this.selector.wakeup();
    }

    public void clientConnected(TcpClient tcpClient) {
        super.notifyClientConnected(tcpClient);
    }

    public void clientDisconnected(TcpClient tcpClient) {
        super.notifyClientDisconnected(tcpClient);
    }

    public void packetReceived(TcpClient tcpClient, ReadPacket readPacket) {
        super.notifyReceivedPacket(tcpClient, readPacket);
    }

    @Override
    public void run() {
        this.isRunning = true;
        super.getLogger().writeLog(LogType.SERVER, "TcpServerNIO", "run", "Server started");
        while (this.isRunning) {
            try {
                // Process any pending changes
                synchronized (this.pendingChangesLock) {
                    for (ChangeRequest change : this.pendingChanges) {
                        switch (change.type) {
                            case ChangeRequest.CHANGEOPS:
                                SelectionKey key = change.socket.keyFor(this.selector);
                                if (key == null) {
                                    super.getLogger().writeLog(LogType.SERVER, "TcpServerNIO", "run", "key is null, try to close");
                                    this.worker.closedSocketChannel(change.socket);
                                } else if (!key.isValid()) {
                                    super.getLogger().writeLog(LogType.SERVER, "TcpServerNIO", "run", "invalid key, try to close");
                                    this.worker.closedSocketChannel(change.socket);
                                } else {
                                    key.interestOps(change.ops);
                                }
                        }
                    }
                    this.pendingChanges.clear();
                }

                // Wait for an event one of the registered channels
                this.selector.select();

                // Iterate over the set of keys for which events are available
                Iterator selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // Check what event is available and deal with it
                    if (key.isAcceptable()) {
                        this.accept(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (ClosedSelectorException e) {
                super.getLogger().writeLog(LogType.SERVER, "TcpServerNIO", "run", "Selector closed");
            } catch (Exception e) {
                super.getLogger().writeLog(e);
            }
        }
        super.getLogger().writeLog(LogType.SERVER, "TcpServerNIO", "run", "Server stopped");
    }

    private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        Socket socket = socketChannel.socket();
        socketChannel.configureBlocking(false);

        // register with worker
        this.worker.openedSocketChannel(socketChannel);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Clear out our read buffer so it's ready for new data
        this.readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(this.readBuffer);

        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            this.worker.closedSocketChannel(socketChannel);

            key.cancel();
            socketChannel.close();
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            this.worker.closedSocketChannel(socketChannel);

            key.channel().close();
            key.cancel();
            return;
        }

        // Hand the data off to our worker thread
        byte[] data = new byte[numRead];

        this.readBuffer.position(0);
        this.readBuffer.get(data);

        this.worker.processData(socketChannel, data);
    }


    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (this.pendingDataLock) {
            ArrayList<ByteBuffer> queue = this.pendingData.get(socketChannel);

            // Write until there's not more data ...
            while (!queue.isEmpty()) {
                ByteBuffer buf = queue.get(0);
                socketChannel.write(buf);
                if (buf.remaining() > 0) {
                    // ... or the socket's buffer fills up
                    break;
                }
                queue.remove(0);
            }

            if (queue.isEmpty()) {
                // We wrote away all data, so we're no longer interested
                // in writing on this socket. Switch back to waiting for
                // data.
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress(super.hostAddress, super.port);
        this.serverChannel.socket().bind(isa);

        // Register the server socket channel, indicating an interest in
        // accepting new connections
        this.serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }
}
