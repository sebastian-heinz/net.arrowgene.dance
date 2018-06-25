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

package net.arrowgene.dance.library.models.group;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Represents a member of a group.
 * <p>
 * The navigation to the associated 'group'-object is missing on purpose
 * to prevent duplicate instances of the same group.
 */
public class GroupMember {

    private int characterId;
    private int groupId;
    private long joinDate;
    private int score;
    private GroupRights groupRights;
    private String characterName;
    private int characterLevel;

    public GroupMember() {
        this.characterId = -1;
        this.groupId = -1;
        this.joinDate = -1;
        this.score = 0;
        this.groupRights = GroupRights.APPLICANT;
    }

    public int getCharacterId() {
        return this.characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public long getJoinDate() {
        return this.joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GroupRights getGroupRights() {
        return this.groupRights;
    }

    public void setGroupRights(GroupRights groupRights) {
        this.groupRights = groupRights;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCharacterLevel() {
        return characterLevel;
    }

    public void setCharacterLevel(int characterLevel) {
        this.characterLevel = characterLevel;
    }

    public String getDateString() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(this.joinDate * 1000);
    }
}
