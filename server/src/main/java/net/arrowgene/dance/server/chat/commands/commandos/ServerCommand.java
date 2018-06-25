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


public class ServerCommand implements ChatCommand {


    @Override
    public String[] exec(String[] command, DanceClient danceClient, DanceServer server) {

        String[] response = new String[1];

        if (command.length == 2) {
            if (command[1].equals("reloadsongs") || command[1].equals("rs")) {
                server.getSongManager().reloadSongs();
                //  server.getSongManager().reloadSongs();
                response[0] = "SongList reloaded";
            } else if (command[1].equals("shutdown") || command[1].equals("kill")) {
                server.stop();
                response[0] = "Our boat is sinking but my words will never reach your chat ...";
            } else if (command[1].equals("save")) {
                server.save();
                response[0] = "Manually triggered World Save";
            } else {
                response[0] = "No valid command found (reloadsongs|rs, shutdown|kill)";
            }
        } else {
            response[0] = "Bad commandos count. ex (/sc [commandos]";
        }

        return response;
    }
}
