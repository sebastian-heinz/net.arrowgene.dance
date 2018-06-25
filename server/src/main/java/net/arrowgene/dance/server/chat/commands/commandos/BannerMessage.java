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

package net.arrowgene.dance.server.chat.commands.commandos;

import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.chat.commands.ChatCommand;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;


public class BannerMessage implements ChatCommand {


    @Override
    public String[] exec(String[] command, DanceClient danceClient, DanceServer server) {

        String[] response = new String[1];

        if (command.length > 4) {

            Packet newPacket = new SendPacket(PacketType.LOBBY_RESPONSE_TOP_MESSAGE_PLAYER_TO_PLAYER);
            newPacket.addInt32(0);
            newPacket.addInt32(Integer.parseInt(command[1]));
            newPacket.addStringNulTerminated(command[2]);
            newPacket.addStringNulTerminated(command[3]);
            String msg = "";
            for (int i = 4; i < command.length; i++) {
                msg += command[i];
                if (i != command.length - 1) {
                    msg += " ";
                }
            }
            newPacket.addStringNulTerminated(msg);

            danceClient.getChannel().sendPacket(newPacket);

            response[0] = "Message is on Air!";
        } else {
            response[0] = "Bad commandos count. ex (/bm [Style] [SenderNick] [ReceiverNick] [Message]";
        }

        return response;
    }
}
