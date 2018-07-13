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

import net.arrowgene.dance.library.models.character.ControllerType;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.builder.RoomPacket;
import net.arrowgene.dance.server.room.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class _2004_x7D4_LOBBY_REQUEST_CREATE_ROOM extends HandlerBase {


    private static final Logger logger = LogManager.getLogger(_2004_x7D4_LOBBY_REQUEST_CREATE_ROOM.class);

    public _2004_x7D4_LOBBY_REQUEST_CREATE_ROOM(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        String roomName = packet.getStringNulTerminated();
        packet.getByte();
        String roomPassword = packet.getStringNulTerminated();

        Room room = client.getChannel().createRoom(this.server.getSongManager(), client, roomName, roomPassword, ControllerType.Keyboard);

        logger.info(String.format("created room '%s' (%s)", room.getName(), client));

        Packet answerPacket = RoomPacket.getInstance().getCreateRoomPacket(client, client.getRoom());
        client.sendPacket(answerPacket);

        return null;
    }
}
