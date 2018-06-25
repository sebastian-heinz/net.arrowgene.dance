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
import net.arrowgene.dance.server.ServerLogger;
import net.arrowgene.dance.server.tcp.io.TcpClientIO;

import java.net.Socket;


public class TPoolClient extends TcpClientIO {

    private PacketState packetState;

    /**
     * Unix timestamp of the last time the server received a network packet from this client.
     */
    private long lastPacketReceived;

    public TPoolClient(Socket socket, ServerLogger logger) {
        super(socket, logger);

        // The client will be disconnected instantly if its new and never read a packet.
        // This gives newly connected clients a grace period to be alive without
        // a valid dance packet for at least 'ServerConfig.getMaxNetworkInactivitySeconds()'.
        this.lastPacketReceived = DanceServer.getUnixTimeNow();
    }

    public PacketState getPacketState() {
        return packetState;
    }

    public void setPacketState(PacketState packetState) {
        this.packetState = packetState;
    }

    public long getLastPacketReceived() {
        return lastPacketReceived;
    }

    public void setLastPacketReceived(long lastPacketReceived) {
        this.lastPacketReceived = lastPacketReceived;
    }
}
