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

package net.arrowgene.dance.server.tcp.io.threadpool;

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.server.tcp.io.ClientManager;
import net.arrowgene.dance.server.tcp.io.TcpClientIO;
import net.arrowgene.dance.server.tcp.io.TcpServerIO;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;


public class TPoolClientManager extends ClientManager {

    private boolean isRunning;
    private LinkedBlockingQueue<TPoolClient> readyClients;
    private LinkedBlockingQueue<TPoolClient> idleClients;
    private TPoolConsumer[] consumers;
    private int consumerCount;
    private TPoolProducer[] producers;
    private int producerCount;

    public TPoolClientManager(DanceServer server, TcpServerIO tcpServerIO) {
        super(server, tcpServerIO);
        this.readyClients = new LinkedBlockingQueue<>();
        this.idleClients = new LinkedBlockingQueue<>();
    }

    public LinkedBlockingQueue<TPoolClient> getReadyClients() {
        return this.readyClients;
    }

    public LinkedBlockingQueue<TPoolClient> getIdleClients() {
        return this.idleClients;
    }

    public void start() {
        if (!this.isRunning) {
            super.getLogger().writeLog(LogType.SERVER, "TPoolClientManager", "start", "Starting Client Manager...");
            this.isRunning = true;
            this.consumerCount = getConfig().getTpConsumerCount();
            this.consumers = new TPoolConsumer[this.consumerCount];
            for (int i = 0; i < this.consumerCount; i++) {
                TPoolConsumer consumer = new TPoolConsumer(i, this.tcpServerIO, this);
                this.consumers[i] = consumer;
                this.consumers[i].start();
            }
            this.producerCount = getConfig().getTpProducerCount();
            this.producers = new TPoolProducer[this.producerCount];
            for (int i = 0; i < this.producerCount; i++) {
                TPoolProducer producer = new TPoolProducer(i, this.tcpServerIO, this);
                this.producers[i] = producer;
                this.producers[i].start();
            }
        } else {
            super.getLogger().writeLog(LogType.SERVER, "TPoolClientManager", "start", "Client Manager already started");
        }
    }

    public void stop() {
        if (this.isRunning) {
            super.getLogger().writeLog(LogType.SERVER, "TPoolClientManager", "stop", "Stopping Client Manager...");
            this.isRunning = false;
            for (int i = 0; i < this.consumerCount; i++) {
                this.consumers[i].stop();
            }
            for (int i = 0; i < this.producerCount; i++) {
                this.producers[i].stop();
            }
        } else {
            super.getLogger().writeLog(LogType.SERVER, "TPoolClientManager", "stop", "Client Manager already stopped");
        }
    }

    public void disconnect(TPoolClient client) {
        super.getLogger().writeLog(LogType.SERVER, "TPoolClientManager", "disconnect", "Disconnecting: " + client.getIdentity());
        client.setAlive(false);
        this.tcpServerIO.clientDisconnected(client);
        try {
            client.getSocket().close();
        } catch (IOException e) {
            super.getLogger().writeLog(e, client);
        }
    }

    @Override
    public void writeDebugInfo() {
        super.getLogger().writeLog(LogType.DEBUG, "TPoolClientManager", "writeDebugInfo", "Idle Clients: " + this.idleClients.size());
        super.getLogger().writeLog(LogType.DEBUG, "TPoolClientManager", "writeDebugInfo", "Ready Clients: " + this.readyClients.size());
    }

    @Override
    public void newConnection(TcpClientIO tcpClient) {
        if (this.isRunning) {
            TPoolClient tPoolClient = new TPoolClient(tcpClient.getSocket(), super.getLogger());
            try {
                tPoolClient.getSocket().setSoTimeout(50);
                tPoolClient.getSocket().setKeepAlive(true);
            } catch (SocketException e) {
                super.getLogger().writeLog(e, tcpClient);
            }
            this.idleClients.add(tPoolClient);
            this.tcpServerIO.clientConnected(tPoolClient);
        }
    }

}
