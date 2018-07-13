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

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class _4008_xFA8_GAME_REQUEST_READY extends HandlerBase {


    private static final Logger logger = LogManager.getLogger(_4008_xFA8_GAME_REQUEST_READY.class);

    public _4008_xFA8_GAME_REQUEST_READY(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        client.getRoom().setReady(client);
        logger.info(String.format("changed ready ready to '%b' in room '%s' (%s)", client.getCharacter().isReady(), client.getRoom().getName(), client));
        return null;
    }
}
