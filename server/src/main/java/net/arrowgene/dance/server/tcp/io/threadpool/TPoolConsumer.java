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
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.tcp.io.TcpServerIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class TPoolConsumer implements Runnable, Thread.UncaughtExceptionHandler {

    private static final Logger logger = LogManager.getLogger(TPoolConsumer.class);

    private static final int joinTimeoutMS = 10;

    private volatile boolean isRunning;

    private TcpServerIO server;
    private TPoolClientManager manager;
    private Thread thread;
    private int number;

    public TPoolConsumer(int number, TcpServerIO server, TPoolClientManager manager) {
        this.server = server;
        this.manager = manager;
        this.number = number;
        isRunning = false;
    }

    public void start() {
        if (!isRunning) {
            logger.info(String.format("Starting TPoolConsumer [%d] ...", number));
            isRunning = true;
            spawn();
        } else {
            logger.info(String.format("TPoolConsumer [%d] already started", number));
        }

    }

    private void spawn() {
        thread = new Thread(this);
        thread.setName("TPoolConsumer [" + number + "]");
        thread.setUncaughtExceptionHandler(this);
        thread.start();
    }

    @SuppressWarnings("Duplicates")
    public void stop() {
        if (isRunning) {
            logger.info(String.format("Stopping TPoolConsumer [%d] ...", number));
            isRunning = false;
            try {
                thread.join(joinTimeoutMS);
                if (thread.isAlive()) {
                    thread.interrupt();
                }
            } catch (InterruptedException e) {
                logger.error(e);
            }
        } else {
            logger.info(String.format("TPoolConsumer [%d] already stopped", number));
        }
    }

    @Override
    public void run() {
        logger.info(String.format("TPoolConsumer [%d] started", number));
        while (isRunning) {
            TPoolClient currentClient = null;
            try {
                currentClient = manager.getReadyClients().take();
            } catch (InterruptedException e) {
                // Blocking queue will need interruption to exit.
                isRunning = false;
                logger.error(e);
            } catch (Exception e) {
                logger.error(e);
            }
            if (currentClient != null) {
                if (currentClient.getAlive()) {
                    ReadPacket readPacket = readPacketBody(currentClient);
                    if (readPacket != null) {
                        currentClient.setLastPacketReceived(DanceServer.getUnixTimeNow());
                        server.packetReceived(currentClient, readPacket);
                    }
                    manager.getIdleClients().add(currentClient);
                } else {
                    manager.disconnect(currentClient);
                }
            } else {
                logger.info(String.format("TPoolConsumer [%d] encountered null client.", number));
            }
        }
        logger.info(String.format("TPoolConsumer [%d] stopped", number));
    }

    private ReadPacket readPacketBody(TPoolClient client) {
        PacketState ps = client.getPacketState();
        ReadPacket readPacket = null;
        short packetSize = ps.getPacketSize();
        short packetId = ps.getPacketId();
        int readBytes = -1;
        int bytesToRead = packetSize - Packet.HEADER_SIZE;
        byte[] body = new byte[bytesToRead];
        try {
            InputStream inStream = client.getSocket().getInputStream();
            readBytes = inStream.read(body, 0, bytesToRead);
        } catch (IOException e) {
            logger.error(String.format("%s (%s)", e.getMessage(), client));
            logger.error(e);
        }
        if (readBytes == bytesToRead) {
            PacketType packetType = PacketType.getType(packetId);
            if (packetType != null) {
                readPacket = new ReadPacket(packetType, body);
            } else {
                // TODO write packet log
                // server.getLogger().writeUnknownPacketLog(packetId, packetSize, body, client);
            }
        } else {
            // TODO write packet log
            // server.getLogger().writeUnknownPacketLog(packetId, packetSize, body, client);
        }
        return readPacket;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error(String.format("TPoolConsumer [%d] died, restarting...", number));
        logger.error(e);
        stop();
        start();
    }
}
