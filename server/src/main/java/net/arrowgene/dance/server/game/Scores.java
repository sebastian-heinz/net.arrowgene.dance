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

package net.arrowgene.dance.server.game;

import net.arrowgene.dance.server.client.DanceClient;

import java.util.ArrayList;


public class Scores {
    private ArrayList<ScoreUser> userScores = new ArrayList<ScoreUser>();

    public void addUser(DanceClient client) {
        userScores.add(new ScoreUser(client));
    }
    public void removeUser(DanceClient client) {
        ScoreUser remove = null;
        for(ScoreUser u : userScores)
        {
            if(u.getClient() == client)
            {
                remove = u;
                break;
            }
        }
        if(remove != null)
        {
            userScores.remove(remove);
        }
    }

    public ScoreUser get(DanceClient client) {
        for (ScoreUser user : userScores) {
            if (user.getClient() == client) {
                return user;
            }
        }

        return null;
    }

    public boolean allFinished() {
        boolean ret = true;

        for (ScoreUser user : userScores) {
            if (!user.isFinished()) {
                ret = false;
                break;
            }
        }

        return ret;
    }
}
