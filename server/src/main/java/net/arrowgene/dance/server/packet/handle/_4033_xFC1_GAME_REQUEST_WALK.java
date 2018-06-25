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

import net.arrowgene.dance.library.models.character.CharacterWalkDirection;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;


public class _4033_xFC1_GAME_REQUEST_WALK extends HandlerBase {

    public _4033_xFC1_GAME_REQUEST_WALK(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        int slotId = client.getCharacter().getRoomSlotId();
        if (slotId >= 0) {
            int direction = packet.getByte();
            int moving = packet.getByte();
            float x = packet.getFloat();
            float y = packet.getFloat();
            float z = packet.getFloat();

            client.getCharacter().setDirection(CharacterWalkDirection.getType(direction));
            client.getCharacter().setMoving(moving);
            client.getCharacter().setX(x);
            client.getCharacter().setY(y);
            client.getCharacter().setZ(z);

            SendPacket answerPacket = new SendPacket(PacketType.GAME_RESPONSE_WALK);
            answerPacket.addByte(slotId); //Slot Id ("0" if you are host, if you join a room it must be set accordingly)
            answerPacket.addByte(direction);
            answerPacket.addByte(moving);
            answerPacket.addFloat(x);
            answerPacket.addFloat(y);
            answerPacket.addFloat(z);
            answerPacket.addByte(0);

            client.getRoom().sendPacket(answerPacket);
        }

        return null;
    }
}
