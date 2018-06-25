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
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.ArrayList;


public class _2057_x809_LOBBY_REQUEST_INVITE_USER extends HandlerBase {

    private ArrayList<String> roomTips;

    public _2057_x809_LOBBY_REQUEST_INVITE_USER(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        String targetName = packet.getStringNulTerminated();

        DanceClient targetClient = client.getChannel().getClientByCharacterName(targetName);
        if(targetClient != null) {
            //TODO checke ob invitations erlaubt sind für den Nutzer
            SendPacket answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_INVITE_USER);
            answerPacket.addStringNulTerminated(client.getCharacter().getName());
            answerPacket.addInt32(client.getRoom().getNumber());
            answerPacket.addByte(0);

            targetClient.sendPacket(answerPacket);
        }
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
