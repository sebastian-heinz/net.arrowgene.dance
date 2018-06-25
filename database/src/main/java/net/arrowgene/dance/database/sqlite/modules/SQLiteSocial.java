
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
import net.arrowgene.dance.library.models.character.SocialEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteSocial {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteSocial(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertBuddy(SocialEntry buddy) throws SQLException {

        PreparedStatement insert = this.controller.createPreparedStatement("INSERT INTO [ag_friend] VALUES (?,?,?,?);");

        insert.setInt(2, buddy.getCharacterId());
        insert.setInt(3, buddy.getSocialId());
        insert.setString(3, buddy.getSocialName());

        insert.execute();
        insert.close();
    }


    public void insertBuddies(List<SocialEntry> buddies) throws SQLException {
        PreparedStatement insert = this.controller.createPreparedStatement("INSERT INTO [ag_friend] VALUES (?,?,?,?);");
        PreparedStatement update = this.controller.createPreparedStatement("UPDATE [ag_friend] SET [character_id]=?, [friend_character_id]=?, [friend_character_name]=? WHERE [friend_id]=?;");

        for (SocialEntry buddy : buddies) {
            if (buddy.getId() > -1) {
                update.clearParameters();
                update.setInt(1, buddy.getCharacterId());
                update.setInt(2, buddy.getSocialId());
                update.setString(3, buddy.getSocialName());
                update.setInt(4, buddy.getId());
                update.execute();
            } else {
                insert.clearParameters();
                insert.setInt(2, buddy.getCharacterId());
                insert.setInt(3, buddy.getSocialId());
                insert.setString(4, buddy.getSocialName());
                insert.execute();
            }
        }

        insert.close();
    }

    public List<SocialEntry> getBuddies(int characterId) throws SQLException {
        ArrayList<SocialEntry> buddies = new ArrayList<SocialEntry>();

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM [ag_friend] WHERE [character_id]=?");
        select.setInt(1, characterId);

        ResultSet res = select.executeQuery();
        while (res.next()) {
            SocialEntry item = this.factory.createSocialEntry(res);
            buddies.add(item);
        }
        res.close();
        select.close();

        return buddies;
    }

    public void deleteBuddy(int buddyId) throws SQLException {

        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM [ag_friend] WHERE [friend_id]=?;");

        delete.setInt(1, buddyId);
        delete.execute();
        delete.close();
    }

    public void deleteBuddies(List<SocialEntry> buddies) throws SQLException {

        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM [ag_friend] WHERE [friend_id]=?;");
        for (SocialEntry buddy : buddies) {
            delete.clearParameters();
            delete.setInt(1, buddy.getId());
            delete.execute();
        }

        delete.close();
    }

    public void insertIgnore(SocialEntry ignored) throws SQLException {

        PreparedStatement insert = this.controller.createPreparedStatement("INSERT INTO [ag_ignore] VALUES (?,?,?,?);");

        insert.setInt(2, ignored.getCharacterId());
        insert.setInt(3, ignored.getSocialId());
        insert.setString(3, ignored.getSocialName());

        insert.execute();
        insert.close();
    }

    public List<SocialEntry> getIgnored(int characterId) throws SQLException {
        ArrayList<SocialEntry> buddies = new ArrayList<SocialEntry>();

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM [ag_ignore] WHERE [character_id]=?");
        select.setInt(1, characterId);

        ResultSet res = select.executeQuery();
        while (res.next()) {
            SocialEntry item = this.factory.createSocialEntry(res);
            buddies.add(item);
        }
        res.close();
        select.close();

        return buddies;
    }

}
