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

import net.arrowgene.dance.library.common.Converter;
import net.arrowgene.dance.log.Logger;
import net.arrowgene.dance.server.tcp.TcpClient;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpClientNIO extends TcpClient {

    private TcpServerNIO server;
    private SocketChannel socket;
    private Logger logger;
    private ByteBuffer packetBuffer;

    public TcpClientNIO(TcpServerNIO server, SocketChannel socket, Logger logger) {
        this.server = server;
        this.socket = socket;
        this.logger = logger;
        this.packetBuffer = null;
    }

    public ByteBuffer getPacketBuffer() {
        return packetBuffer;
    }

    public void setPacketBuffer(ByteBuffer packetBuffer) {
        this.packetBuffer = packetBuffer;
    }

    @Override
    public void sendPacket(byte[] data) {
        this.server.send(this.socket, data);
    }

    @Override
    public String getIdentity() {
        SocketAddress address = null;
        try {
            address = this.socket.getRemoteAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String identity = Converter.getAddressString(address);
        if (identity == null) {
            identity = "Unknown";
        }
        return identity;
    }
}
