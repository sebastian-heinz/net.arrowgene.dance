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

import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.log.Logger;
import net.arrowgene.dance.server.ServerLogger;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class PacketWorker implements Runnable {

    private final Object workerLock = new Object();
    private final Object clientLock = new Object();
    private volatile boolean isRunning;

    private LinkedList<ServerDataEvent> queue;
    private Thread workerThread;
    private HashMap<SocketChannel, TcpClientNIO> clients;
    private TcpServerNIO tcpServerNIO;
    private ServerLogger logger;

    public PacketWorker(TcpServerNIO tcpServerNIO, ServerLogger logger) {
        this.tcpServerNIO = tcpServerNIO;
        this.logger = logger;
        this.queue = new LinkedList<ServerDataEvent>();
        this.clients = new HashMap<SocketChannel, TcpClientNIO>();
    }

    public void start() {
        this.workerThread = new Thread(this);
        this.workerThread.setName("PacketWorkerThread");
        this.workerThread.start();
    }

    public void stop() {
        this.isRunning = false;
    }

    public void writeDebugInfo() {
        synchronized (this.workerLock) {
            this.logger.writeLog(LogType.DEBUG, "PacketWorker", "writeDebugInfo", "Queue Size: " + this.queue.size());
        }
        synchronized (this.clientLock) {
            this.logger.writeLog(LogType.DEBUG, "PacketWorker", "writeDebugInfo", "Clients: " + this.clients.size());
        }
    }

    public void openedSocketChannel(SocketChannel socketChannel) {
        TcpClientNIO client;
        synchronized (this.clientLock) {
            client = new TcpClientNIO(this.tcpServerNIO, socketChannel, this.logger);
            this.clients.put(socketChannel, client);
        }
        this.tcpServerNIO.clientConnected(client);
    }

    public void closedSocketChannel(SocketChannel socketChannel) {
        TcpClientNIO client;
        synchronized (this.clientLock) {
            client = this.clients.remove(socketChannel);
        }
        this.tcpServerNIO.clientDisconnected(client);
    }

    public void processData(SocketChannel socketChannel, byte[] data) {
        TcpClientNIO client;
        synchronized (this.clientLock) {
            client = this.clients.get(socketChannel);
        }

        if (client != null) {
            synchronized (this.workerLock) {
                this.queue.add(new ServerDataEvent(client, data));
                this.workerLock.notify();
            }
        } else {
            this.logger.writeLog(LogType.ERROR, "PacketWorker", "processData", "Unknown Client");
        }
    }

    public void run() {
        ServerDataEvent dataEvent;
        this.isRunning = true;

        while (this.isRunning) {

            // Wait for data to become available
            synchronized (this.workerLock) {
                while (this.queue.isEmpty()) {
                    try {
                        this.workerLock.wait();
                    } catch (InterruptedException e) {
                        this.isRunning = false;
                    }
                }
                dataEvent = this.queue.remove(0);
            }

            ArrayList<ReadPacket> readPackets = this.craftPackets(dataEvent);

            for (ReadPacket readPacket : readPackets) {
                this.tcpServerNIO.packetReceived(dataEvent.getClient(), readPacket);
            }
        }
    }

    private ArrayList<ReadPacket> craftPackets(ServerDataEvent dataEvent) {

        TcpClientNIO client = dataEvent.getClient();


        ArrayList<ReadPacket> packets = new ArrayList<ReadPacket>();

        ByteBuffer buffer = client.getPacketBuffer();
        if (buffer == null) {
            buffer = ByteBuffer.wrap(dataEvent.getData());
        }

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        boolean read = true;
        while (read) {
            if (buffer.remaining() >= 2) {

                short packetSize = buffer.getShort();
                int toRead = packetSize - 2;

                if (buffer.remaining() >= toRead) {
                    short packetId = buffer.getShort();
                    toRead = packetSize - 4;

                    byte[] packetBody = new byte[toRead];
                    buffer.get(packetBody);

                    PacketType packetType = PacketType.getType(packetId);

                    if (packetType != null) {
                        ReadPacket readPacket = new ReadPacket(packetType, packetBody);
                        packets.add(readPacket);
                    } else {
                        this.logger.writeUnknownPacketLog(packetId, packetSize, packetBody, client);
                    }
                } else {
                    read = false;
                }
            } else {
                read = false;
            }
        }

        if (buffer.remaining() > 0) {
            buffer.compact();
            client.setPacketBuffer(buffer);
        } else {
            client.setPacketBuffer(null);
        }

        return packets;
    }
}
