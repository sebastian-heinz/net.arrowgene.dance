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


public class PacketSender implements ChatCommand {

    @Override
    public String[] exec(String[] command, DanceClient danceClient, DanceServer server) {


        String[] response = new String[1];

        if (command.length > 2) {

            int packetId;
            try {
                packetId = Integer.parseInt(command[1]);
            } catch (NumberFormatException e) {
                packetId = -1;
            }

            PacketType packetType = PacketType.getType(packetId);

            if (packetType != null) {

                String hex = "";
                for (int i = 2; i < command.length; i++) {
                    hex += command[i];
                }

                if (!hex.isEmpty()) {
                    Packet newPacket = new SendPacket(packetType);
                    newPacket.addHEXString(hex);
                    danceClient.getChannel().sendPacket(newPacket);

                    response[0] = newPacket.toString();
                } else {
                    response[0] = "Bad Hex(" + hex.length() + "): " + hex;
                }
            } else {
                if (packetId == -1) {
                    response[0] = "PacketId not found.:";
                } else {
                    response[0] = "Bad PacketId:" + packetId;
                }
            }
        } else {
            response[0] = "Bad commandos count. ex (/p [PacketId] [HEX]";
        }
        return response;
    }
}


