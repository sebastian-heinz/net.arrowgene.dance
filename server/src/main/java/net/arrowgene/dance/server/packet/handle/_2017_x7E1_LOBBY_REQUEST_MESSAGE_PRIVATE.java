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


public class _2017_x7E1_LOBBY_REQUEST_MESSAGE_PRIVATE extends HandlerBase {

    public _2017_x7E1_LOBBY_REQUEST_MESSAGE_PRIVATE(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient danceClient) {

        String recipientName = packet.getStringNulTerminated();
        String message = " " + packet.getStringNulTerminated();

        DanceClient recipient = this.server.getClientController().getClientByCharacterName(recipientName);

        ChatMessage chatMessage = new ChatMessage(message, danceClient, danceClient.getCharacter().getName(), ChatType.CHANNEL, recipientName, recipient);
        this.server.getChatManager().handleMessage(chatMessage);

        if (chatMessage.isDeliver()) {
            if (recipient == null) {

                // respond: 'is not in the current channel'
                // TODO we know better, hes actually not online!
                // TODO Private messages should work server wide, so maybe we deliver a better message
                SendPacket answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_MESSAGE_PRIVATE);
                answerPacket.addInt32(1);
                answerPacket.addStringNulTerminated(recipientName);
                answerPacket.addByte(0);
                danceClient.sendPacket(answerPacket);

                // TODO Private Message, recipient is not online. (Maybe we send him an post? , but beware spam!)
            } else {
                SendPacket recipientAnswerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_MESSAGE_PRIVATE);
                recipientAnswerPacket.addInt32(0);
                recipientAnswerPacket.addStringNulTerminated(danceClient.getCharacter().getName());
                recipientAnswerPacket.addStringNulTerminated(message);
                recipient.sendPacket(recipientAnswerPacket);

                SendPacket senderAnswerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_MESSAGE_PRIVATE);
                senderAnswerPacket.addInt32(0);
                senderAnswerPacket.addStringNulTerminated("@" + recipientName);
                senderAnswerPacket.addStringNulTerminated(message);
                danceClient.sendPacket(senderAnswerPacket);
            }
        }

        return null;
    }
}
