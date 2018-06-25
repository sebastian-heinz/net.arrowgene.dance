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
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.builder.GamePacket;


public class _4027_xFBB_GAME_REQUEST_LOADING_READY extends HandlerBase {

    public _4027_xFBB_GAME_REQUEST_LOADING_READY(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        client.setLoadingDone(true);
        if(client.getRoom().areAllClientsLoadingDone()) {
            Packet answerPacket = GamePacket.getInstance().getLoadingReadyPacket();
            client.getRoom().sendPacket(answerPacket);
        }

        return null;

        // Calendar c = Calendar.getInstance();
        // c.add(Calendar.DAY_OF_MONTH, 0);
        // c.set(Calendar.HOUR_OF_DAY, 0);
        // c.set(Calendar.MINUTE, 0);
        // c.set(Calendar.SECOND, 0);
        // c.set(Calendar.MILLISECOND, 0);
        // int dayMillis = (int) (System.currentTimeMillis() - c.getTimeInMillis());

        // Packet answerPacket = new Packet(PacketType.GAME_RESPONSE_LOADING_READY);
        // answerPacket.addInt32(0);
        // answerPacket.addInt32(dayMillis);//Zeit in Millisekunden (Uhrzeit)
        // //answerPacket.addHEXString("d6991e1e00");
        // answerPacket.addByte(0);
        // answerPacket.addByte(0);

        // answerPacket.addByte(0);

        // client.write(answerPacket);

        // return new SendPacket[0];
    }
}
