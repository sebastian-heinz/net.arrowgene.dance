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
import net.arrowgene.dance.server.tcp.io.ClientManager;
import net.arrowgene.dance.server.tcp.io.TcpClientIO;
import net.arrowgene.dance.server.tcp.io.TcpServerIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;


public class TPoolClientManager extends ClientManager {


    private static final Logger logger = LogManager.getLogger(TPoolClientManager.class);

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
            logger.info("Starting Client Manager...");
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
            logger.info("Client Manager already started");
        }
    }

    public void stop() {
        if (this.isRunning) {
            logger.info("Stopping Client Manager...");
            this.isRunning = false;
            for (int i = 0; i < this.consumerCount; i++) {
                this.consumers[i].stop();
            }
            for (int i = 0; i < this.producerCount; i++) {
                this.producers[i].stop();
            }
        } else {
            logger.info("Client Manager already stopped");
        }
    }

    public void disconnect(TPoolClient client) {
        logger.info(String.format("Disconnecting (%s)", client.getIdentity()));
        client.setAlive(false);
        this.tcpServerIO.clientDisconnected(client);
        try {
            client.getSocket().close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void writeDebugInfo() {
        logger.debug(String.format("Idle Clients: %d", idleClients.size()));
        logger.debug(String.format("Ready Clients: %d", readyClients.size()));
    }

    @Override
    public void newConnection(TcpClientIO tcpClient) {
        if (this.isRunning) {
            TPoolClient tPoolClient = new TPoolClient(tcpClient.getSocket());
            try {
                tPoolClient.getSocket().setSoTimeout(50);
                tPoolClient.getSocket().setKeepAlive(true);
            } catch (SocketException e) {
                logger.error("%s (%s)", e.getMessage(), tcpClient);
                logger.error(e);
            }
            this.idleClients.add(tPoolClient);
            this.tcpServerIO.clientConnected(tPoolClient);
        }
    }

}
