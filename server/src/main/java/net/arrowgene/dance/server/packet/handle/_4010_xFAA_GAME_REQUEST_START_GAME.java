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
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.builder.RoomPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class _4010_xFAA_GAME_REQUEST_START_GAME extends HandlerBase {


    private static final Logger logger = LogManager.getLogger(_4010_xFAA_GAME_REQUEST_START_GAME.class);

    public _4010_xFAA_GAME_REQUEST_START_GAME(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        if (client.getRoom().areAllActiveClientsReady()) {
            client.getRoom().startSong();

            Packet answerPacket = RoomPacket.getInstance().getStartGamePacket(client.getRoom().getSong());

            client.getRoom().sendPacket(answerPacket);

            logger.info(String.format("started game in room '%s' (%s)", client.getRoom().getName(), client));

        } else {
            Packet answerPacket = RoomPacket.getInstance().getStartGameErrorPacket(1);

            client.sendPacket(answerPacket);

        }

        return null;

        // Packet answerPacket = new Packet(PacketType.GAME_RESPONSE_START_GAME);
        // if (areAllReady()) {


        //     SongManager manager = server.getSongManager();
        //     Song currentSong = manager.getSong(song);
        //     byte[] dancepadData = manager.getDancepadData(currentSong);
        //     byte[] keyboardData = manager.getKeyboardData(currentSong);

        //     answerPacket.addBytes(dancepadData);
        //     answerPacket.addBytes(keyboardData);
        //     answerPacket.addHEXString(currentSong.getKey());

        //     answerPacket.addByte(0);
        //     answerPacket.addByte(0);
        //     answerPacket.addByte(0);

        //     //System.out.println(answerPacket.toString());
        //     sendToAll(answerPacket);
        // }

        // return new SendPacket[0];
    }
}
