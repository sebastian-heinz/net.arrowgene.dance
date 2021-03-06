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
import net.arrowgene.dance.library.models.group.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteGroup {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteGroup(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertGroups(List<Group> groups) throws SQLException {

        PreparedStatement insert = this.controller
            .createPreparedStatement("INSERT INTO [ag_group] VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
        PreparedStatement update = this.controller.createPreparedStatement(
            "UPDATE [ag_group] SET [group_name]=?,[group_date]=?,[group_score]=?,[group_slogan]=?,[group_introduction]=?,[group_icon]=?,[group_leader_id]=?,[group_notice_text]=?,[group_notice_title]=?,[group_notice_date]=?,[group_max_members]=? WHERE [group_id]=?;");

        for (Group group : groups) {
            if (group.getId() > -1) {
                update.clearParameters();
                update.setString(1, group.getName());
                update.setLong(2, group.getCreationDate());
                update.setInt(3, group.getScore());
                update.setString(4, group.getSlogan());
                update.setString(5, group.getIntroduction());
                update.setInt(6, group.getIcon());
                update.setInt(7, group.getLeaderId());
                update.setString(8, group.getNoticeBoardText());
                update.setString(9, group.getNoticeBoardTitle());
                update.setLong(10, group.getNoticeDate());
                update.setLong(11, group.getId());
                update.setInt(12,group.getMaxMembers());
                update.execute();
            } else {
                insert.clearParameters();
                insert.setString(2, group.getName());
                insert.setLong(3, group.getCreationDate());
                insert.setInt(4, group.getScore());
                insert.setString(5, group.getSlogan());
                insert.setString(6, group.getIntroduction());
                insert.setInt(7, group.getIcon());
                insert.setInt(8, group.getLeaderId());
                insert.setString(9, group.getNoticeBoardText());
                insert.setString(10, group.getNoticeBoardTitle());
                insert.setLong(11, group.getNoticeDate());
                insert.setInt(12,group.getMaxMembers());
                insert.execute();
            }
        }

        insert.close();
        update.close();
    }


    public void insertGroup(Group group) throws SQLException {

        if (group.getId() > -1) {
            PreparedStatement update = this.controller.createPreparedStatement(
                "UPDATE [ag_group] SET [group_name]=?,[group_date]=?,[group_score]=?,[group_slogan]=?,[group_introduction]=?,[group_icon]=?,[group_leader_id]=?,[group_notice_text]=?,[group_notice_title]=?,[group_notice_date]=?,[group_max_members]=? WHERE [group_id]=?;");

            update.setString(1, group.getName());
            update.setLong(2, group.getCreationDate());
            update.setInt(3, group.getScore());
            update.setString(4, group.getSlogan());
            update.setString(5, group.getIntroduction());
            update.setInt(6, group.getIcon());
            update.setInt(7, group.getLeaderId());
            update.setString(8, group.getNoticeBoardText());
            update.setString(9, group.getNoticeBoardTitle());
            update.setLong(10, group.getNoticeDate());
            update.setLong(11, group.getId());
            update.setInt(12,group.getMaxMembers());
            update.execute();

            int id = this.controller.getAutoIncrement(update);
            group.setId(id);

            update.close();
        } else {
            PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT INTO [ag_group] VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");

            insert.setString(2, group.getName());
            insert.setLong(3, group.getCreationDate());
            insert.setInt(4, group.getScore());
            insert.setString(5, group.getSlogan());
            insert.setString(6, group.getIntroduction());
            insert.setInt(7, group.getIcon());
            insert.setInt(8, group.getLeaderId());
            insert.setString(9, group.getNoticeBoardText());
            insert.setString(10, group.getNoticeBoardTitle());
            insert.setLong(11, group.getNoticeDate());
            insert.setInt(12,group.getMaxMembers());
            insert.execute();

            int id = this.controller.getAutoIncrement(insert);
            group.setId(id);

            insert.close();
        }
    }

    public List<Group> getGroups() throws SQLException {
        List<Group> groups = new ArrayList<Group>();

        PreparedStatement select = this.controller.createPreparedStatement("SELECT * FROM [ag_group] INNER JOIN [ag_character] ON ag_group.group_leader_id = ag_character.character_id");

        ResultSet rs = select.executeQuery();
        while (rs.next()) {
            Group group = this.factory.createGroup(rs);
            groups.add(group);
        }

        rs.close();
        select.close();

        return groups;
    }

    public void deleteGroup(int groupId) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM [ag_group] WHERE [group_id]=?");
        delete.setInt(1, groupId);
        delete.execute();
        delete.close();
    }

    public void deleteGroups(List<Group> groups) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM [ag_group] WHERE [group_id]=?");

        for (Group group : groups) {
            delete.clearParameters();
            delete.setInt(1, group.getId());
            delete.execute();
        }

        delete.close();
    }

}
