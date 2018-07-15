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

package net.arrowgene.dance.database.maria.modules;

import net.arrowgene.dance.database.maria.MariaDbController;
import net.arrowgene.dance.database.maria.MariaDbFactory;
import net.arrowgene.dance.library.models.song.FavoriteSong;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbFavoriteSong {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbFavoriteSong(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertFavoriteSong(FavoriteSong favoriteSong) throws SQLException {
        if (favoriteSong.getId() > -1) {
            PreparedStatement update = controller.createPreparedStatement("UPDATE `dance_favorite_song` SET `song_id`=?, `character_id`=? WHERE `id`=?;");
            update.setInt(1, favoriteSong.getSongId());
            update.setInt(2, favoriteSong.getCharacterId());
            update.setInt(3, favoriteSong.getId());
            update.execute();
            update.close();
        } else {
            PreparedStatement insert = controller.createPreparedStatement("INSERT INTO `dance_favorite_song` VALUES (?,?,?);");
            insert.setInt(2, favoriteSong.getSongId());
            insert.setInt(3, favoriteSong.getCharacterId());
            insert.execute();
            insert.close();
        }
    }

    public void insertFavoriteSongs(List<FavoriteSong> favoriteSongs) throws SQLException {
        PreparedStatement insert = controller.createPreparedStatement("INSERT INTO `dance_favorite_song` VALUES (?,?,?);");
        PreparedStatement update = controller.createPreparedStatement("UPDATE `dance_favorite_song` SET `song_id`=?, `character_id`=? WHERE `id`=?;");
        for (FavoriteSong favoriteSong : favoriteSongs) {
            if (favoriteSong.getId() > -1) {
                update.clearParameters();
                update.setInt(1, favoriteSong.getSongId());
                update.setInt(2, favoriteSong.getCharacterId());
                update.setInt(3, favoriteSong.getId());
                update.execute();
            } else {
                insert.clearParameters();
                insert.setInt(2, favoriteSong.getSongId());
                insert.setInt(3, favoriteSong.getCharacterId());
                insert.execute();
            }
        }
        update.close();
        insert.close();
    }

    public List<FavoriteSong> getFavoriteSongs(int characterId) throws SQLException {
        ArrayList<FavoriteSong> favoriteSongs = new ArrayList<FavoriteSong>();
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_favorite_song` WHERE `character_id`=?");
        select.setInt(1, characterId);
        ResultSet res = select.executeQuery();
        while (res.next()) {
            FavoriteSong item = factory.createFavoriteSong(res);
            favoriteSongs.add(item);
        }
        res.close();
        select.close();
        return favoriteSongs;
    }

    public void deleteFavoriteSong(int favoriteSongId) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM `dance_favorite_song` WHERE `id`=?");
        delete.setInt(1, favoriteSongId);
        delete.execute();
        delete.close();
    }
}
