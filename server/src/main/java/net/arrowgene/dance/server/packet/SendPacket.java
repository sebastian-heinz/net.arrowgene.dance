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

import net.arrowgene.dance.server.client.DanceClient;

import java.util.ArrayList;


public class SendPacket extends Packet {

    private ArrayList<DanceClient> receivers;
    private PacketType packetType;

    public SendPacket(PacketType packetType, DanceClient... receivers) {
        super((short) packetType.getNumValue());

        this.packetType = packetType;
        this.receivers = new ArrayList<DanceClient>();

        for (DanceClient receiver : receivers) {
            this.addReceiver(receiver);
        }
    }

    public void addReceiver(DanceClient client) {
        this.receivers.add(client);
    }

    public ArrayList<DanceClient> getReceivers() {
        return receivers;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }
}
