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
import net.arrowgene.dance.server.chat.ChatMessage;
import net.arrowgene.dance.server.chat.ChatType;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;


public class _2012_x7DC_LOBBY_REQUEST_MESSAGE_CHANNEL extends HandlerBase {

    public _2012_x7DC_LOBBY_REQUEST_MESSAGE_CHANNEL(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient danceClient) {

        String message = packet.getStringNulTerminated();
        ChatMessage chatMessage = new ChatMessage(message, danceClient, danceClient.getCharacter().getName(), ChatType.CHANNEL);
        this.server.getChatManager().handleMessage(chatMessage);
        if (chatMessage.isDeliver()) {
            SendPacket answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_MESSAGE_CHANNEL);
            answerPacket.addStringNulTerminated(danceClient.getCharacter().getName());
            answerPacket.addStringNulTerminated(message);
            answerPacket.addByte(0);
            danceClient.getChannel().sendPacket(answerPacket);
        }

        return null;
    }
}
