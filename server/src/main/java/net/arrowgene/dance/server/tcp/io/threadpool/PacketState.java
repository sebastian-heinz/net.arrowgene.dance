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

public class PacketState {

    private short packetSize;
    private short packetId;
    private PacketStateType packetStateType;

    public PacketState(byte[] header, PacketStateType packetStateType) {
        this.packetStateType = packetStateType;
        this.packetSize = (short) (((header[1] & 0xff) << 8) | (header[0] & 0xff));
        this.packetId = (short) (((header[3] & 0xff) << 8) | (header[2] & 0xff));
    }

    public short getPacketSize() {
        return packetSize;
    }

    public short getPacketId() {
        return packetId;
    }

    public PacketStateType getPacketStateType() {
        return packetStateType;
    }
}
