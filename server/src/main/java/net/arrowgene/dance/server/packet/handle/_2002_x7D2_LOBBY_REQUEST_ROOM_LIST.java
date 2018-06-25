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
import net.arrowgene.dance.server.channel.Channel;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.room.Room;

import java.util.ArrayList;


public class _2002_x7D2_LOBBY_REQUEST_ROOM_LIST extends HandlerBase {

    public _2002_x7D2_LOBBY_REQUEST_ROOM_LIST(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {


        SendPacket answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_ROOM_LIST);

        Channel channel = client.getChannel();
        if (channel != null) {

            ArrayList<Room> rooms = channel.getRooms();
            answerPacket.addInt32(rooms.size());
            answerPacket.addByte(0);

            for (Room r : rooms) {
                answerPacket.addInt32(r.getNumber());
                answerPacket.addByte(r.getState().getNumValue());
                answerPacket.addStringNulTerminated(r.getName());

                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);

                answerPacket.addByte(0);
                answerPacket.addByte((byte) (r.isLocked() ? 1 : 0));
                answerPacket.addByte(r.getControllerType().getNumValue());
                answerPacket.addByte(0);

                answerPacket.addByte(r.getMode().getNumValue());


                answerPacket.addByte(r.getActiveCount());
                answerPacket.addByte(r.getActiveMaleCount());
                answerPacket.addByte(r.getMaxPlayer());

                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);

                answerPacket.addByte(r.getColor());
                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);
            }
        } else {
            getLogger().writeLog(LogType.ERROR, "_2002_x7D2_LOBBY_REQUEST_ROOM_LIST", "handle", "Client has no channel", client);
        }

        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }
}
