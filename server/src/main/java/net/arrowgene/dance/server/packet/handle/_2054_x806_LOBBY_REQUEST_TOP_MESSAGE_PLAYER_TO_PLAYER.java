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
import net.arrowgene.dance.server.lobby.LobbyTopMessageStyle;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.ArrayList;
import java.util.Random;


public class _2054_x806_LOBBY_REQUEST_TOP_MESSAGE_PLAYER_TO_PLAYER extends HandlerBase {

    private ArrayList<String> roomTips;

    public _2054_x806_LOBBY_REQUEST_TOP_MESSAGE_PLAYER_TO_PLAYER(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        int id = packet.getInt16();
        String targetName = packet.getStringNulTerminated();
        String fromName = client.getCharacter().getName();
        String message = packet.getStringNulTerminated();

        SendPacket answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_TOP_MESSAGE_PLAYER_TO_PLAYER);
        answerPacket.addInt32(0);
        answerPacket.addInt32(LobbyTopMessageStyle.ANGEL.getNumValue());
        answerPacket.addStringNulTerminated(targetName);
        answerPacket.addStringNulTerminated(fromName);
        answerPacket.addStringNulTerminated(message);

        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addInt32(0);

        client.getChannel().sendPacket(answerPacket);
        /*
        answerPacket.addInt32(0);
        answerPacket.addStringNulTerminated(targetName);
        answerPacket.addStringNulTerminated(message);
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);
*/
        return null;
    }
}
