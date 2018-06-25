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

package net.arrowgene.dance.server.packet.builder;

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;

public class ChannelPacket {

    private static ChannelPacket instance = new ChannelPacket();

    public static ChannelPacket getInstance() {
        return instance;
    }

    public Packet getAnnounceLeave(Character character) {
        Packet packet = new SendPacket(PacketType.LOBBY_RESPONSE_CHANNEL_USER_LEAVE);
        packet.addStringNulTerminated(character.getName());
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addByte(0);
        return packet;
    }

    public Packet getAnnounceJoin(Character character) {
        Packet packet = new SendPacket(PacketType.LOBBY_RESPONSE_CHANNEL_USER_NEW);
        packet.addStringNulTerminated(character.getName());
        packet.addInt32(character.getLevel());
        packet.addByte(character.getSex().getNumValue());
        packet.addByte(1); //Keyboard maybe?
        packet.addByte(character.getFlag());
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addByte(0);
        return packet;
    }

}
