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

package net.arrowgene.dance.server.tcp;

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerConfig;
import net.arrowgene.dance.server.packet.ReadPacket;

import java.net.InetAddress;
import java.util.ArrayList;


public abstract class TcpServer {

    public static final int MAX_PACKET_SIZE = Short.MAX_VALUE;

    private ArrayList<DisconnectedListener> disconnectedListeners;
    private ArrayList<ConnectedListener> connectedListeners;
    private ArrayList<ReceivedPacketListener> receivedPacketListener;
    private DanceServer server;
    protected int port;
    protected InetAddress hostAddress;

    public TcpServer(DanceServer server) {
        this.server = server;
        this.disconnectedListeners = new ArrayList<DisconnectedListener>();
        this.connectedListeners = new ArrayList<ConnectedListener>();
        this.receivedPacketListener = new ArrayList<ReceivedPacketListener>();
    }

    public ServerConfig getConfig() {
        return this.server.getServerConfig();
    }

    public abstract void start();

    public abstract void stop();

    public abstract void writeDebugInfo();

    public void addReceivedPacketListener(ReceivedPacketListener listener) {
        this.receivedPacketListener.add(listener);
    }

    public void addDisconnectedListener(DisconnectedListener listener) {
        this.disconnectedListeners.add(listener);
    }

    public void addConnectedListener(ConnectedListener listener) {
        this.connectedListeners.add(listener);
    }

    protected void notifyClientDisconnected(TcpClient tcpClient) {
        for (DisconnectedListener listener : this.disconnectedListeners) {
            listener.clientDisconnected(tcpClient);
        }
    }

    protected void notifyClientConnected(TcpClient tcpClient) {
        for (ConnectedListener listener : this.connectedListeners) {
            listener.clientConnected(tcpClient);
        }
    }

    protected void notifyReceivedPacket(TcpClient tcpClient, ReadPacket readPacket) {
        // TODO consider creating a deep copy of readPacket
        for (ReceivedPacketListener listener : this.receivedPacketListener) {
            listener.receivedPacket(tcpClient, readPacket);
        }
    }
}
