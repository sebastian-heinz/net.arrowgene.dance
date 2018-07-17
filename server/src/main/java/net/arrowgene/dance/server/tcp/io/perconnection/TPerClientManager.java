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

package net.arrowgene.dance.server.tcp.io.perconnection;

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.tcp.io.ClientManager;
import net.arrowgene.dance.server.tcp.io.TcpClientIO;
import net.arrowgene.dance.server.tcp.io.TcpServerIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class TPerClientManager extends ClientManager {


    private static final Logger logger = LogManager.getLogger(TPerClientManager.class);

    private final Object clientAccessLock = new Object();
    private volatile boolean isRunning;

    private ArrayList<TcpClientIO> clients;

    public TPerClientManager(DanceServer server, TcpServerIO tcpServerIO) {
        super(server, tcpServerIO);
        this.isRunning = false;
        this.clients = new ArrayList<>();
    }

    public void start() {
        this.clients.clear();
        this.isRunning = true;
        logger.info("Client Manager started, accepting clients");
    }

    public void stop() {
        this.isRunning = false;
        logger.info("Stop, disconnecting clients...");
        synchronized (this.clientAccessLock) {
            for (TcpClientIO tcpClientIO : this.clients) {
                tcpClientIO.setAlive(false);
                try {
                    tcpClientIO.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("Client Manager stopped");
    }

    @Override
    public void writeDebugInfo() {
        logger.debug(String.format("Clients: %d", clients.size()));
    }

    @Override
    public void newConnection(TcpClientIO tcpClientIO) {
        if (this.isRunning) {
            synchronized (this.clientAccessLock) {
                this.clients.add(tcpClientIO);
            }
            TPerClientTask clientTask = new TPerClientTask(super.tcpServerIO, tcpClientIO);
            Thread thread = new Thread(clientTask);
            thread.setName(String.format("Client Thread: %s", tcpClientIO));
            thread.start();
        }
    }
}
