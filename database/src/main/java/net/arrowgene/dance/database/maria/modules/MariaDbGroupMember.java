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
import net.arrowgene.dance.library.models.group.GroupMember;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbGroupMember {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbGroupMember(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertGroupMember(GroupMember groupMember) throws SQLException {
        // CharacterId is the PrimaryKey constrain, triggering replace
        PreparedStatement insert = controller.createPreparedStatement("INSERT OR REPLACE INTO `dance_group_member` VALUES (?,?,?,?,?);");
        insert.clearParameters();
        insert.setInt(1, groupMember.getCharacterId());
        insert.setInt(2, groupMember.getGroupId());
        insert.setLong(3, groupMember.getJoinDate());
        insert.setInt(4, groupMember.getScore());
        insert.setInt(5, groupMember.getGroupRights().getNumValue());
        insert.execute();
        insert.close();
    }

    public void insertGroupMembers(List<GroupMember> groupMembers) throws SQLException {
        // CharacterId is the PrimaryKey constrain, triggering replace
        PreparedStatement insert = controller.createPreparedStatement("INSERT OR REPLACE INTO `dance_group_member` VALUES (?,?,?,?,?);");
        for (GroupMember groupMember : groupMembers) {
            insert.clearParameters();
            insert.setInt(1, groupMember.getCharacterId());
            insert.setInt(2, groupMember.getGroupId());
            insert.setLong(3, groupMember.getJoinDate());
            insert.setInt(4, groupMember.getScore());
            insert.setInt(5, groupMember.getGroupRights().getNumValue());
            insert.execute();
        }
        insert.close();
    }

    public List<GroupMember> getGroupMembers(int groupId) throws SQLException {
        List<GroupMember> groupMembers = new ArrayList<>();
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_group_member` INNER JOIN `dance_character` ON dance_group_member.character_id = dance_character.id WHERE `group_id`=?;");
        ResultSet rs = select.executeQuery();
        while (rs.next()) {
            GroupMember group = factory.createGroupMember(rs);
            groupMembers.add(group);
        }
        rs.close();
        select.close();
        return groupMembers;
    }

    public List<GroupMember> getGroupMembers() throws SQLException {
        List<GroupMember> groupMembers = new ArrayList<>();
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_group_member` INNER JOIN `dance_character` ON dance_group_member.character_id = dance_character.id;");
        ResultSet rs = select.executeQuery();
        while (rs.next()) {
            GroupMember group = factory.createGroupMember(rs);
            groupMembers.add(group);
        }
        rs.close();
        select.close();
        return groupMembers;
    }

    public void deleteGroupMember(int characterId) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM `dance_group_member` WHERE `character_id`=?");
        delete.setInt(1, characterId);
        delete.execute();
        delete.close();
    }

    public void deleteGroupMembers(List<GroupMember> groupMembers) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM `dance_group_member` WHERE `character_id`=?");
        for (GroupMember groupMember : groupMembers) {
            delete.clearParameters();
            delete.setInt(1, groupMember.getCharacterId());
            delete.execute();
        }
        delete.close();
    }
}
