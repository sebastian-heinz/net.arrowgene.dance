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

package net.arrowgene.dance.server.song;


import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class SongManager extends ServerComponent {


    private static final Logger logger = LogManager.getLogger(SongManager.class);
    private ArrayList<Song> songs = new ArrayList<Song>();

    public SongManager(DanceServer server) {
        super(server);
        this.songs = new ArrayList<>();
    }

    @Override
    public void load() {
        this.reloadSongs();
    }

    @Override
    public void save() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {

    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        logger.debug(String.format("Songs:  %d", songs.size()));
    }

    public void reloadSongs() {
        this.songs.clear();
        List<Song> songs = super.getDatabase().getSongs();
        for (Song s : songs) {
            this.songs.add(s);
        }
    }

    public Song getSong(int id) {
        id = id & 0xffff;
        Song retSong = null;
        for (Song s : songs) {
            if (s.getSongId() == id) {
                retSong = s;
                break;
            }
        }
        return retSong;
    }
}
