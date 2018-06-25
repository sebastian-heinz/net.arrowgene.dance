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

package net.arrowgene.dance.server.packet.handle;

import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;


public class _3028_xBD4_ROOM_REQUEST_ENTER_ROOM_1 extends HandlerBase {

    public _3028_xBD4_ROOM_REQUEST_ENTER_ROOM_1(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        Packet answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_ENTER_ROOM_1);
        /*
        answerPacket.addInt32(2);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(1);

        answerPacket.addInt32(0);
        answerPacket.addByte(0);*/
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;

        //    Packet answerPacket = new Packet(PacketType.ROOM_RESPONSE_ENTER_ROOM_1);
        /*
        answerPacket.addByte(getUserCount());
        answerPacket.addInt32(0);
        answerPacket.addByte(0);
        answerPacket.addInt32(1);
        answerPacket.addByte(0);
        answerPacket.addInt32(2);
        answerPacket.addByte(0);
        answerPacket.addInt32(3);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);*/
        // Count + Byte
        // Byte + Slot ID // LOOP Players

        //  answerPacket.addByte(0);
        // answerPacket.addByte(0);


        //     answerPacket.addByte(0);

        //    client.write(answerPacket);


    }
}
