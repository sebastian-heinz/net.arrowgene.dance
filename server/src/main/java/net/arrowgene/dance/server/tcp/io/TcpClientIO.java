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

import net.arrowgene.dance.library.common.Converter;
import net.arrowgene.dance.server.ServerLogger;
import net.arrowgene.dance.server.tcp.TcpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class TcpClientIO extends TcpClient {

    private Socket socket;
    private ServerLogger logger;

    public TcpClientIO(Socket socket, ServerLogger logger) {
        super();
        this.logger = logger;
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void sendPacket(byte[] data) {
        try {
            OutputStream outStream = this.socket.getOutputStream();
            outStream.write(data);
        } catch (IOException e) {
            this.logger.writeLog(e, this);
        }
    }

    @Override
    public String getIdentity() {
        SocketAddress address = this.socket.getRemoteSocketAddress();
        String identity = Converter.getAddressString(address);
        if (identity == null) {
            identity = "Unknown";
        }
        return identity;
    }
}
