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

package net.arrowgene.dance.server.packet;

import net.arrowgene.dance.library.common.ByteBuffer;


public class Packet extends ByteBuffer {

    public final static int HEADER_SIZE = 4;

    private short packetId;

    public Packet(short packetId, byte[] payload) {
        this(packetId);
        super.addBytes(payload);
        this.setCurrentPosStart();
    }

    public Packet(short packetId) {
        super();
        this.packetId = packetId;
        this.setCurrentPosStart();
    }

    public short getPacketId() {
        return packetId;
    }

    @Override
    public byte[] getAllBytes() {
        this.writeHeader();
        return super.getAllBytes();
    }

    @Override
    public void setCurrentPosStart() {
        super.setCurrentPos(Packet.HEADER_SIZE);
    }

    public void writeHeader() {
        super.setCurrentPos(0);
        super.addInt16(super.getSize());
        super.addInt16(this.packetId);
    }

}
