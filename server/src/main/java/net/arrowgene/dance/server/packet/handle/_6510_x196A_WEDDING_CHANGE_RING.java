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

import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.wedding.ChangeRingMsg;

/**
 * Request when the wedding menu opens, displays the info when the button 'My Application" is pressed.
 */
public class _6510_x196A_WEDDING_CHANGE_RING extends HandlerBase {


    public _6510_x196A_WEDDING_CHANGE_RING(DanceServer server) {
        super(server);
    }


    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        int ring = packet.getByte();
        String message = packet.getStringNulTerminated();

        RingType ringType = RingType.getType(ring);

        ChangeRingMsg changeRingMessage = this.server.getLoveMagistrate().changeRing(client.getCharacter().getCharacterId(), message, ringType);

        SendPacket answerPacket = new SendPacket(PacketType.WEDDING_RESPONSE_CHANGE_RING);
        answerPacket.addInt32(changeRingMessage.getNumValue());
        answerPacket.addByte(0);
        client.sendPacket(answerPacket);

        return null;
    }
}
