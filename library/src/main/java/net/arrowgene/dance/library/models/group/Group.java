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
import java.util.ArrayList;
import java.util.List;

public class Group {

    public static final int INITIAL_GROUP_SIZE = 15;

    private final Object userLock = new Object();

    private int id;
    private int leaderId;
    private String name;
    private String slogan;
    private String introduction;
    private String noticeBoardTitle;
    private String noticeBoardText;
    private long creationDate;
    private long noticeDate;
    private int icon;
    private int score;
    private String leaderName;
    private int ranking;
    private List<GroupMember> groupUsers;
    private int maxMembers;


    public Group() {
        this.groupUsers = new ArrayList<GroupMember>();
        this.id = -1;
        this.ranking = -1;
        this.score = 0;
        this.noticeDate = -1;
        this.noticeBoardText = "";
        this.noticeBoardTitle = "";
        this.maxMembers = INITIAL_GROUP_SIZE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getNoticeBoardTitle() {
        return noticeBoardTitle;
    }

    public void setNoticeBoardTitle(String noticeBoardTitle) {
        this.noticeBoardTitle = noticeBoardTitle;
    }

    public String getNoticeBoardText() {
        return noticeBoardText;
    }

    public void setNoticeBoardText(String noticeBoardText) {
        this.noticeBoardText = noticeBoardText;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(long noticeDate) {
        this.noticeDate = noticeDate;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    /**
     * @return Date string of the NoticeDate-Property (yyyy-MM-dd)
     */
    public String getNoticeDateString() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(this.noticeDate * 1000);
    }

    /**
     * @return Date string of the CreationDate-Property (yyyy-MM-dd)
     */
    public String getCreationDateString() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(this.creationDate * 1000);
    }

    /**
     * @return All GroupMembers (Leader, Member and Applicants)
     */
    public List<GroupMember> getAllPeople() {
        List<GroupMember> members = null;
        synchronized (this.userLock) {
            members = new ArrayList<>(this.groupUsers);
        }
        return members;
    }

    /**
     * Adds a user to the group.
     *
     * @param groupMember
     */
    public void addGroupUser(GroupMember groupMember) {
        synchronized (this.userLock) {
            if (!this.groupUsers.contains(groupMember)) {
                this.groupUsers.add(groupMember);
            }
        }
    }

    /**
     * Removes a user from the group.
     *
     * @param groupMember
     */
    public void removeGroupUser(GroupMember groupMember) {
        synchronized (this.userLock) {
            if (groupMember != null) {
                this.groupUsers.remove(groupMember);
            }
        }
    }

    /**
     * @return All users with the GroupRights.MEMBER
     */
    public List<GroupMember> getMembers() {
        List<GroupMember> members = new ArrayList<GroupMember>();
        for (GroupMember member : this.getAllPeople()) {
            if (member.getGroupRights() == GroupRights.MEMBER || member.getGroupRights() == GroupRights.LEADER) {
                members.add(member);
            }
        }
        return members;
    }

    /**
     * @return All users with GroupRights.APPLICANT
     */
    public List<GroupMember> getApplicants() {
        List<GroupMember> members = new ArrayList<GroupMember>();
        for (GroupMember member : this.getAllPeople()) {
            if (member.getGroupRights() == GroupRights.APPLICANT) {
                members.add(member);
            }
        }
        return members;
    }

    /**
     * @return The leader.
     */
    public GroupMember getLeader() {
        GroupMember leader = null;
        for (GroupMember member : this.getAllPeople()) {
            if (member.getGroupRights() == GroupRights.LEADER) {
                leader = member;
                break;
            }
        }
        return leader;
    }

}
