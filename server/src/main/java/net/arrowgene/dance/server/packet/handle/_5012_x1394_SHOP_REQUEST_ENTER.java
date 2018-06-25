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
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;


public class _5012_x1394_SHOP_REQUEST_ENTER extends HandlerBase {

    public _5012_x1394_SHOP_REQUEST_ENTER(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        //TODO muss User aus channel geworfen werden oder aus dem Raum?
        //aus dem raum, bzw sollte man den shop nicht betreten können, ohne
        //den raum zu verlassen. wenn man sich im shop befindet, wird man auch noch
        //in der lobbyliste angezeigt. Im D!O hat man dadurch manchmal private nachrichten verpasst.
        //Evtl können wir die user aus der liste nehmen wenn sie nicht in der lage sind auf nachrichten
        //oder einladungen zu reagieren. klingt nach einem guten feature!
        if (client.getAccount() != null && client.getCharacter() != null) {
            // TODO Fehlerhaft
            SendPacket answerPacket = new SendPacket(PacketType.SHOP_RESPONSE_ENTER);

            answerPacket.addInt32(0x1b60);
            answerPacket.addInt32(0xfd7);
            answerPacket.addInt32(0);
            answerPacket.addInt32(0x1f);
            answerPacket.addByte(0);

            client.sendPacket(answerPacket);
        }


        return null;
    }
}
