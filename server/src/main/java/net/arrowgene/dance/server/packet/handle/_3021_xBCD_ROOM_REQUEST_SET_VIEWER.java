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
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;


public class _3021_xBCD_ROOM_REQUEST_SET_VIEWER extends HandlerBase {

    public _3021_xBCD_ROOM_REQUEST_SET_VIEWER(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        if(client.getRoom() != null)
        {
            client.getRoom().setViewer(client);
        }

        return null;

        //  byte oldSlot = (byte) getSlot(client);

        //  for (int i = 0; i < activeUsers.length; i++) {
        //      if (activeUsers[i] == client) {
        //          activeUsers[i] = null;
        //          announceRoomInChannel();
        //          addViewer(client);
        //      }
        //  }

        //  if (oldSlot == getHostSlot()) {
        //      updateHost();
        //  }

        //  Packet answerPacket = new Packet(PacketType.ROOM_RESPONSE_SET_VIEWER);
        //  answerPacket.addByte(oldSlot); //Slot of User before Moving Slot
        //  answerPacket.addByte(getSlot(client)); // Slot of User after Moving
        //  answerPacket.addByte(getHostSlot()); // Hostslot (Hostzeichen)
        //  answerPacket.addByte(0);

        //  sendToAll(answerPacket);

        //  return new SendPacket[0];
    }
}
