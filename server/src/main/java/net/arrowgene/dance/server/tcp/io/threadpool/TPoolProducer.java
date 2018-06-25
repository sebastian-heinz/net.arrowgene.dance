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


import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.tcp.io.TcpServerIO;

import java.io.IOException;
import java.io.InputStream;

public class TPoolProducer implements Runnable {

    private static final int joinTimeoutMS = 10;

    private volatile boolean isRunning;

    private TcpServerIO server;
    private TPoolClientManager manager;
    private Thread thread;
    private int number;

    public TPoolProducer(int number, TcpServerIO server, TPoolClientManager manager) {
        this.server = server;
        this.manager = manager;
        this.number = number;
        this.isRunning = false;
    }

    public void start() {
        if (!this.isRunning) {
            this.server.getLogger().writeLog(LogType.SERVER, "TPoolProducer", "start", "Starting TPoolProducer [" + this.number + "] ...");
            this.isRunning = true;
            thread = new Thread(this);
            thread.setName("TPoolProducer [" + this.number + "]");
            thread.start();
        } else {
            this.server.getLogger().writeLog(LogType.SERVER, "TPoolProducer", "start", "TPoolProducer [" + this.number + "] already started");
        }
    }

    @SuppressWarnings("Duplicates")
    public void stop() {
        if (this.isRunning) {
            this.server.getLogger().writeLog(LogType.SERVER, "TPoolProducer", "stop", "Stopping TPoolProducer [" + this.number + "] ...");
            this.isRunning = false;
            try {
                this.thread.join(joinTimeoutMS);
                if (this.thread.isAlive()) {
                    this.thread.interrupt();
                }
            } catch (InterruptedException e) {
                this.server.getLogger().writeLog(e);
            }
        } else {
            this.server.getLogger().writeLog(LogType.SERVER, "TPoolProducer", "stop", "TPoolProducer [" + this.number + "] already stopped");
        }
    }

    @Override
    public void run() {
        this.server.getLogger().writeLog(LogType.SERVER, "TPoolProducer", "run", "Client Manager started");
        while (this.isRunning) {
            TPoolClient client = null;
            try {
                if (this.server.getConfig().getTpProducerSleepMS() > 0) {
                    Thread.sleep(this.server.getConfig().getTpProducerSleepMS());
                }
                client = this.manager.getIdleClients().take();
            } catch (InterruptedException e) {
                this.isRunning = false;
                this.server.getLogger().writeLog(e);
            } catch (Exception e) {
                this.server.getLogger().writeLog(e);
            }
            if (client != null) {
                if (client.getAlive()) {
                    PacketState ps = this.readPacketHeader(client);
                    PacketStateType pst = ps.getPacketStateType();
                    if (pst == PacketStateType.READY) {
                        client.setPacketState(ps);
                        this.manager.getReadyClients().add(client);
                    } else if (pst == PacketStateType.IDLE) {
                        long duration = DanceServer.getUnixTimeNow() - client.getLastPacketReceived();
                        if (duration > this.server.getConfig().getMaxNetworkInactivitySeconds()) {
                            this.server.getLogger().writeLog(LogType.CLIENT, String.format("Killing client due to no network activity for %s seconds", duration), client);
                            this.manager.disconnect(client);
                        } else {
                            this.manager.getIdleClients().add(client);
                        }
                    } else if (pst == PacketStateType.ERROR) {
                        this.manager.disconnect(client);
                    } else if (pst == PacketStateType.DISCONNECTED) {
                        this.manager.disconnect(client);
                    } else {
                        this.server.getLogger().writeLog(LogType.ERROR, "TPoolProducer", "run", "Entered unhandled case");
                        this.manager.disconnect(client);
                    }
                } else {
                    this.manager.disconnect(client);
                }
            } else {
                this.server.getLogger().writeLog(LogType.ERROR, "TPoolProducer", "run", "Encountered null client.");
            }
        }
        this.server.getLogger().writeLog(LogType.SERVER, "TPoolProducer", "run", "Client Manager stopped");
    }

    private PacketState readPacketHeader(TPoolClient client) {
        PacketStateType pst;
        byte[] header = new byte[Packet.HEADER_SIZE];
        try {
            InputStream inStream = client.getSocket().getInputStream();
            if (inStream.available() > 0) {
                int readBytes = inStream.read(header, 0, Packet.HEADER_SIZE);
                if (readBytes == Packet.HEADER_SIZE) {
                    pst = PacketStateType.READY;
                } else if (readBytes == -1) {
                    // End of stream
                    pst = PacketStateType.DISCONNECTED;
                } else {
                    // TODO consider fragmented packages? maybe we receive only 2 bytes of the header on this read?
                    this.server.getLogger().writeDataLog("Invalid Header Log", readBytes, header, client);
                    pst = PacketStateType.ERROR;
                }
            } else {
                pst = PacketStateType.IDLE;
            }
        } catch (IOException e) {
            this.server.getLogger().writeLog(e, client);
            pst = PacketStateType.ERROR;
        }
        return new PacketState(header, pst);
    }

}
