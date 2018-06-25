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

package net.arrowgene.dance.server.group;

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.group.GroupRights;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.log.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all group related requests.
 * <p>
 * Don't manually add or remove {@link GroupMember} of a {@link Group}, always use the methods provided by the {@link GroupManager}.
 * The {@link GroupManager} will synchronise and ensure a consistent state of all {@link Group} and {@link GroupMember} states.
 * <p>
 * The {@link GroupManager} is designed to execute all tasks with minimum information.
 * This might introduce an overhead in performance (more lookups) but provides flexibility in return.
 * <p>
 * All actions are performed unconditionally.
 * Calling {@link #createGroup} will not check whether the {@link Character} owns the group disband card.
 * This applies to all requirements, and allows for administrative tools to perform changes.
 * It also means those requirements needs to be checked in place if needed.
 */
public class GroupManager extends ServerComponent {

    public static final int ITEM_CREATE_GROUP_CARD_ID = 3858;
    public static final int ITEM_DISBAND_GROUP_CARD_ID = 3862;
    public static final int MIN_GROUP_LEVEL = 25;
    public static final int MAX_TITLE_LENGTH = 35;
    public static final int MAX_TEXT_LENGTH = 200;

    private final Object groupsLock = new Object();
    private final Object groupMembersLock = new Object();
    private final Object groupCreateLock = new Object();
    private List<Group> groups;
    private List<GroupMember> groupMembers;
    private boolean activated;

    /**
     * Constructs a {@code GroupManager} and loads the latest state from the database.
     */
    public GroupManager(DanceServer server) {
        super(server);
        this.groups = new ArrayList<Group>();
        this.groupMembers = new ArrayList<GroupMember>();
        this.activated = true;
    }

    /**
     * Load the state from the database.
     * <p>
     * Calling this method will loose all non saved states.
     */
    @Override
    public void load() {

        synchronized (this.groupsLock) {
            synchronized (this.groupMembersLock) {
                // Acquire Locks

                this.groupMembers.clear();
                this.groupMembers = super.getDatabase().getGroupMembers();

                this.groups.clear();
                this.groups = super.getDatabase().getGroups();

                for (GroupMember member : this.groupMembers) {
                    for (Group group : this.groups) {
                        if (member.getGroupId() == group.getId()) {
                            group.addGroupUser(member);
                            break;
                        }
                    }
                }
                // Release Locks
            }
        }
    }

    /**
     * Save the state to the database.
     */
    @Override
    public void save() {
        super.getDatabase().syncGroups(this.getGroups());
        super.getDatabase().syncGroupMembers(this.getGroupMembers());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {
        if (client.getCharacter() != null) {
            GroupMember groupMember = this.getGroupMemberByCharacterId(client.getCharacter().getCharacterId());
            if (groupMember != null) {
                Group group = this.server.getGroupManager().getGroupById(groupMember.getGroupId());
                client.setGroupMember(groupMember);
                client.setGroup(group);
            }
        }
    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        synchronized (this.groupsLock) {
            getLogger().writeLog(LogType.DEBUG, "GroupManager", "writeDebugInfo", "Groups: " + this.groups.size());
        }
        synchronized (this.groupMembersLock) {
            getLogger().writeLog(LogType.DEBUG, "GroupManager", "writeDebugInfo", "Group Members: " + this.groupMembers.size());
        }
    }

    /**
     * Returns {@code true} if {@code GroupMember} is active.
     *
     * @return {@code true} if {@link GroupMember} is active
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Get a momentary list of all existing groups.
     * The list is a copy and save to modify.
     *
     * @return {@link List<Group>} of all existing {@link Group Groups}
     */
    public List<Group> getGroups() {
        List<Group> groups = null;
        synchronized (this.groupsLock) {
            groups = new ArrayList<>(this.groups);
        }
        return groups;
    }

    /**
     * Get a momentary list of all existing group members.
     * The list is a copy and save to modify.
     *
     * @return a {@link List<GroupMember>} of all existing {@link GroupMember GroupMembers}
     */
    public List<GroupMember> getGroupMembers() {
        List<GroupMember> groupMembers = null;
        synchronized (this.groupMembersLock) {
            groupMembers = new ArrayList<>(this.groupMembers);
        }
        return groupMembers;
    }

    /**
     * Retrieve a {@code Group} by id.
     *
     * @param id id of the {@link Group}
     * @return {@link Group} or {@code null} if no {@link Group} with the given id exists
     */
    public Group getGroupById(int id) {
        Group result = null;
        List<Group> groups = this.getGroups();
        for (Group group : groups) {
            if (group.getId() == id) {
                result = group;
                break;
            }
        }
        this.getGroupMemberByCharacterId(1);
        return result;
    }

    /**
     * Retrieve a {@code GroupMember} by character id.
     *
     * @param characterId id of {@link Character}
     * @return {@link GroupMember} or {@code null} if no {@link Group} with the given character id exists
     */
    public GroupMember getGroupMemberByCharacterId(int characterId) {
        GroupMember result = null;
        List<GroupMember> members = this.getGroupMembers();
        for (GroupMember member : members) {
            if (member.getCharacterId() == characterId) {
                result = member;
                break;
            }
        }
        return result;
    }

    /**
     * Retrieve a {@code GroupMember} by character name.
     *
     * @param characterName name of {@link Character}
     * @return {@link GroupMember} or {@code null} if no {@link GroupMember} with the given character name exists
     */
    public GroupMember getGroupMemberByCharacterName(String characterName) {
        GroupMember result = null;
        List<GroupMember> members = this.getGroupMembers();
        for (GroupMember member : members) {
            if (member.getCharacterName().toLowerCase().equals(characterName.toLowerCase())) {
                result = member;
                break;
            }
        }
        return result;
    }

    /**
     * Retrieve a {@code Group} by name.
     *
     * @param groupName name of {@link Group}
     * @return {@link Group} or {@code null} if no {@link Group} with the given name exists
     */
    public Group getGroupByName(String groupName) {
        Group result = null;
        List<Group> groups = this.getGroups();
        for (Group group : groups) {
            if (group.getName().toLowerCase().equals(groupName.toLowerCase())) {
                result = group;
                break;
            }
        }
        return result;
    }

    /**
     * Check whether a {@code Group} with a given name exists.
     *
     * @param groupName name of the {@link Group} in question
     * @return {@code true} if the {@link Group} exits otherwise {@code false}
     */
    public boolean isGroupExisting(String groupName) {
        boolean result = false;
        List<Group> groups = this.getGroups();
        for (Group group : groups) {
            if (group.getName().toLowerCase().equals(groupName.toLowerCase())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Creates a new {@code Group}.
     * <p>
     * If the name already exists or it couldn't be stored inside the db, {@code null} will be returned.
     *
     * @param leader       {@link Character} who creates the {@link Group}
     * @param groupName    name of the {@link Group}
     * @param slogan       a public slogan capturing the spirit of the {@link Group}
     * @param introduction an introduction text visible in the public {@link Group} details
     * @param icon         icon representing the {@link Group}
     * @return the newly created {@link Group} or {@code null} on failure
     */
    public Group createGroup(Character leader, String groupName, String slogan, String introduction, int icon) {
        Group group = null;

        synchronized (this.groupCreateLock) {
            if (!this.isGroupExisting(groupName)) {
                group = new Group();
                group.setCreationDate(DanceServer.getUnixTimeNow());
                group.setIcon(icon);
                group.setIntroduction(introduction);
                group.setLeaderId(leader.getCharacterId());
                group.setLeaderName(leader.getName());
                group.setName(groupName);
                group.setSlogan(slogan);

                boolean success = super.getDatabase().insertGroup(group);

                if (success) {
                    this.addGroup(group);
                    this.joinGroup(leader.getCharacterId(), group.getId(), GroupRights.LEADER);
                } else {
                    group = null;
                }
            }
        }
        return group;
    }

    /**
     * Adds a {@code Character} to a {@code Group}.
     * <p>
     * Additionally assigns the {@link GroupMember} and associated {@link Group} to
     * the appropriate {@link DanceClient} if the client is online.
     * <p>
     * {@code null} will be returned if the storage operation failed or
     * the {@link Character} is already a {@link GroupMember} of another {@link Group}.
     *
     * @param characterId
     * @param groupId
     * @param rights
     * @return The newly created {@link Group} or {@code null} on failure
     */
    public GroupMember joinGroup(int characterId, int groupId, GroupRights rights) {

        Character character = super.server.getCharacterManager().getCharacterById(characterId);
        if (character == null) {
            super.getLogger().writeLog(LogType.GROUP, "GroupManager", "joinGroup",
                "Character not found: " + characterId);
            return null;
        }

        Group group = this.getGroupById(groupId);
        if (group == null) {
            super.getLogger().writeLog(LogType.GROUP, "GroupManager", "joinGroup",
                "Group could not be found: " + groupId);
            return null;
        }

        GroupMember member = new GroupMember();
        member.setCharacterId(characterId);
        member.setGroupId(groupId);
        member.setJoinDate(DanceServer.getUnixTimeNow());
        member.setCharacterName(character.getName());
        member.setCharacterLevel(character.getLevel());
        if (rights != null) {
            member.setGroupRights(rights);
        }

        boolean success = super.getDatabase().insertGroupMember(member);
        if (!success) {
            super.getLogger().writeLog(LogType.GROUP, "GroupManager", "joinGroup",
                "GroupMember could not be stored for character: " + character.getName());
            return null;
        }

        group.addGroupUser(member);
        this.addGroupMember(member);

        DanceClient client = super.server.getClientController().getClientByCharacterId(characterId);
        if (client != null) {
            client.setGroup(group);
            client.setGroupMember(member);
        }

        return member;
    }

    /**
     * Removes a {@code Character} from a {@code Group}.
     * <p>
     * Additionally removes the {@link GroupMember} and {@link Group} from
     * the appropriate {@link DanceClient} if the client is online.
     * <p>
     * If the leader leaves, the group and all members will be destroyed.
     *
     * @param characterId id of the {@link Character} to remove from its {@link Group}
     */
    public void leaveGroup(int characterId) {

        GroupMember member = this.getGroupMemberByCharacterId(characterId);
        Group group = this.getGroupById(member.getGroupId());

        this.removeGroupMember(member);
        group.removeGroupUser(member);

        DanceClient client = super.server.getClientController().getClientByCharacterId(characterId);
        if (client != null) {
            client.setGroup(null);
            client.setGroupMember(null);
        }

        if (group.getLeaderId() == characterId) {
            this.disbandGroup(group.getId());
        }
    }

    /**
     * Remove all {@link GroupMember} and disband the {@link Group}
     *
     * @param groupId id of the {@link Group} to disband
     */
    public void disbandGroup(int groupId) {

        Group group = this.getGroupById(groupId);

        for (GroupMember member : group.getMembers()) {
            group.removeGroupUser(member);
            this.removeGroupMember(member);

            DanceClient client = super.server.getClientController().getClientByCharacterId(member.getCharacterId());
            if (client != null) {
                client.setGroup(null);
                client.setGroupMember(null);
            }
        }

        this.removeGroup(group);
    }

    /**
     * Add a {@code Group} to the internal list.
     *
     * @param group the {@link Group} to add
     */
    private void addGroup(Group group) {
        synchronized (this.groupsLock) {
            this.groups.add(group);
        }
    }

    /**
     * Remove a {@code Group} from the internal list.
     *
     * @param group the {@link Group} to remove
     */
    private void removeGroup(Group group) {
        synchronized (this.groupsLock) {
            this.groups.remove(group);
        }
    }

    /**
     * Add a {@code GroupMember} to the internal list.
     *
     * @param groupMember the {@link GroupMember} to add
     */
    private void addGroupMember(GroupMember groupMember) {
        synchronized (this.groupMembersLock) {
            this.groupMembers.add(groupMember);
        }
    }

    /**
     * Remove a {@code GroupMember} from the internal list.
     *
     * @param groupMember the {@link GroupMember} to remove
     */
    private void removeGroupMember(GroupMember groupMember) {
        synchronized (this.groupMembersLock) {
            this.groupMembers.remove(groupMember);
        }
    }

}
