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

package net.arrowgene.dance.database.sqlite.modules;

import net.arrowgene.dance.database.sqlite.SQLiteController;
import net.arrowgene.dance.database.sqlite.SQLiteFactory;
import net.arrowgene.dance.library.models.song.Song;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteSong {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteSong(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertSong(Song song) throws SQLException {

        PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT OR REPLACE INTO [ag_song] VALUES (?,?,?,?,?,?,?,?,?,?);");

        insert.setInt(1, song.getSongId());
        insert.setInt(2, song.getFileId());
        insert.setString(3, song.getName());
        insert.setString(4, song.getKey());
        insert.setInt(5, song.getNoteK().getCrc32());
        insert.setInt(6, song.getNoteK().getKey());
        insert.setInt(7, song.getNoteK().getSize());
        insert.setInt(8, song.getNoteT().getCrc32());
        insert.setInt(9, song.getNoteT().getKey());
        insert.setInt(10, song.getNoteT().getSize());

        insert.execute();
        insert.close();

    }

    public void insertSongs(List<Song> songs) throws SQLException {

        PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT OR REPLACE INTO [ag_song] VALUES (?,?,?,?,?,?,?,?,?,?);");

        for (Song song : songs) {
            insert.clearParameters();
            insert.setInt(1, song.getSongId());
            insert.setInt(2, song.getFileId());
            insert.setString(3, song.getName());
            insert.setString(4, song.getKey());
            insert.setInt(5, song.getNoteK().getCrc32());
            insert.setInt(6, song.getNoteK().getKey());
            insert.setInt(7, song.getNoteK().getSize());
            insert.setInt(8, song.getNoteT().getCrc32());
            insert.setInt(9, song.getNoteT().getKey());
            insert.setInt(10, song.getNoteT().getSize());
            insert.execute();
        }

        insert.close();
    }

    public List<Song> getSongs() throws SQLException {
        ArrayList<Song> songs = new ArrayList<Song>();

        PreparedStatement select = this.controller.createPreparedStatement("SELECT * FROM [ag_song]");

        ResultSet res = select.executeQuery();
        while (res.next()) {
            Song song = this.factory.createSong(res);
            songs.add(song);
        }

        res.close();
        select.close();

        return songs;
    }

    public void deleteSong(int songId) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM [ag_song] WHERE [song_id]=?");
        delete.setInt(1, songId);
        delete.execute();
        delete.close();
    }

}
