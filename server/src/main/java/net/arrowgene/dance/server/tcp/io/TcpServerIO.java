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

package net.arrowgene.dance.server.tcp.io;


import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.tcp.TcpClient;
import net.arrowgene.dance.server.tcp.TcpServer;
import net.arrowgene.dance.server.tcp.io.perconnection.TPerClientManager;
import net.arrowgene.dance.server.tcp.io.threadpool.TPoolClientManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerIO extends TcpServer implements Runnable {


    private static final Logger logger = LogManager.getLogger(TcpServerIO.class);

    public static boolean isSocketClosedException(Exception ex) {
        boolean result = false;
        if (ex != null && ex.getMessage() != null) {
            result = ex.getMessage().equalsIgnoreCase("socket closed");
        }
        return result;
    }

    private volatile boolean isRunning;
    private ServerSocket socket;
    private Thread acceptThread;
    private ClientManager clientManager;

    public TcpServerIO(DanceServer server) {
        super(server);

        switch (super.getConfig().getClientManagerType()) {
            case ThreadPerConnection:
                this.clientManager = new TPerClientManager(server, this);
                break;
            case ThreadPool:
                this.clientManager = new TPoolClientManager(server, this);
                break;
            default:
                logger.error("Manager Type not found, check settings for [ClientManagerType]value");
                break;
        }
    }

    public void start() {
        if (!this.isRunning) {
            super.port = super.getConfig().getPort();
            super.hostAddress = super.getConfig().getHostAddress();

            try {
                this.socket = new ServerSocket(this.port, 50, this.hostAddress);
            } catch (IOException ex) {
                logger.error(ex);
            }

            if (this.socket != null) {
                this.clientManager.start();
                this.acceptThread = new Thread(this);
                this.acceptThread.setName("AcceptSocketConnections");
                this.acceptThread.start();
            }
        } else {
            logger.warn("Server already started");
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.clientManager.stop();
            this.isRunning = false;
            try {
                this.socket.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        } else {
            logger.warn("Server already stopped");
        }
    }

    @Override
    public void writeDebugInfo() {
        this.clientManager.writeDebugInfo();
    }

    @Override
    public void run() {
        this.isRunning = true;
        logger.info("Server started");
        while (this.isRunning) {
            try {
                Socket clientSocket = this.socket.accept();
                TcpClientIO tcpClient = new TcpClientIO(clientSocket);
                this.clientManager.newConnection(tcpClient);
            } catch (IOException e) {
                if (TcpServerIO.isSocketClosedException(e)) {
                    logger.warn("Accept socket closed");
                } else {
                    logger.error(e);
                }
            }
        }
        logger.info("Server stopped");
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
}
