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

package net.arrowgene.dance.server.packet.builder;

import net.arrowgene.dance.library.models.song.FavoriteSong;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.List;

public class SongPacket {

    private static SongPacket instance = new SongPacket();

    public static SongPacket getInstance() {
        return instance;
    }


    public Packet getFavoriteSongsPacket(List<FavoriteSong> songs) {
        Packet packet = new SendPacket(PacketType.ROOM_RESPONSE_FAVORITE_SONG_LIST);
        packet.addInt32(songs.size()); //Count

        for (FavoriteSong s : songs) {
            packet.addInt16(s.getSongId());
            packet.addByte(0);
            packet.addByte(1);
        }
        // answerPacket.addInt32(0x01000109); //Air Play
        // answerPacket.addInt32(0x01000095); //Luxury
        // answerPacket.addInt32(0x010000B3); //Show me your Love
        packet.addByte(0);
        return packet;
    }

}
