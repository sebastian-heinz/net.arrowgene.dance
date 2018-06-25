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

import net.arrowgene.dance.library.models.song.FavoriteSong;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.builder.SongPacket;

import java.util.List;


public class _3019_xBCB_ROOM_REQUEST_FAVORITE_SONG_LIST extends HandlerBase {

    public _3019_xBCB_ROOM_REQUEST_FAVORITE_SONG_LIST(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        List<FavoriteSong> songs = client.getFavoriteSongs();

        Packet answerPacket = SongPacket.getInstance().getFavoriteSongsPacket(songs);

        client.sendPacket(answerPacket);

        return null;

        // TODO: liste laden und senden
        // Packet answerPacket = new Packet(PacketType.ROOM_RESPONSE_FAVORITE_SONG_LIST); //0xbcc

        // answerPacket.addInt32(4); //Count

        // answerPacket.addInt32(0x0100008C); //Thank You
        // answerPacket.addInt32(0x01000109); //Air Play
        // answerPacket.addInt32(0x01000095); //Luxury
        // answerPacket.addInt32(0x010000B3); //Show me your Love
        // answerPacket.addByte(0);

        // client.write(answerPacket);

        // return new SendPacket[0];
    }
}
