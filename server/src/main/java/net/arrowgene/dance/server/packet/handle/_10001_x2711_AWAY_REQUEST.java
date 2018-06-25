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
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

/**
 * The client will send this packet every 15 seconds,
 * if there was no interaction with the client.
 */
public class _10001_x2711_AWAY_REQUEST extends HandlerBase {

    private static final int resetTimeout = 20;

    public _10001_x2711_AWAY_REQUEST(DanceServer server) {
        super(server);
    }


    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        long now = DanceServer.getUnixTimeNow();
        long diff = now - client.getLastAwayPing();
        if (diff > resetTimeout) {
            client.setTotalSecondsAway(0);
        } else {
            client.setTotalSecondsAway(client.getTotalSecondsAway() + diff);
            getLogger().writeLog(LogType.CLIENT, String.format("Away %s Seconds", client.getTotalSecondsAway()), client);
            if (client.getTotalSecondsAway() > server.getServerConfig().getMaxAwaySeconds()) {
                client.disconnect();
                getLogger().writeLog(LogType.CLIENT, "Disconnected by Server because of inactivity", client);
            }
        }
        client.setLastAwayPing(now);
        return null;
    }
}
