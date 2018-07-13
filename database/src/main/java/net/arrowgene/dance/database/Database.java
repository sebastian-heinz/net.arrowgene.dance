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

package net.arrowgene.dance.database;

import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountSettings;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.library.models.channel.ChannelDetails;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.SocialEntry;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.library.models.item.ShopItem;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.library.models.song.FavoriteSong;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;

import java.util.ArrayList;
import java.util.List;

public abstract class Database {

    public abstract boolean insertAccount(Account account);

    public abstract boolean insertPassword(String accountName, String newPasswordHash);

    public abstract Account getAccount(String accountName);

    public abstract Account getAccount(int accountId);

    public abstract Account getAccount(String accountName, String passwordHash);

    public abstract List<ChannelDetails> getChannels();

    public abstract Character getCharacter(String characterName);

    public abstract List<Character> getCharactersByUserId(int userId);

    public abstract Character getCharacterById(int characterId);

    public abstract boolean insertCharacter(Character character);

    public abstract boolean insertFavoriteSong(FavoriteSong favoriteSong);

    public abstract boolean insertFavoriteSongs(List<FavoriteSong> favoriteSongs);

    public abstract List<FavoriteSong> getFavoriteSongs(int characterId);

    public abstract boolean deleteFavoriteSong(int favoriteSongId);

    public abstract boolean insertGroups(List<Group> groups);

    public abstract boolean insertGroup(Group group);

    public abstract List<Group> getGroups();

    public abstract boolean deleteGroup(int groupId);

    public abstract boolean deleteGroups(List<Group> groups);

    public abstract boolean insertGroupMember(GroupMember groupMember);

    public abstract boolean insertGroupMembers(List<GroupMember> groupMembers);

    public abstract List<GroupMember> getGroupMembers(int groupId);

    public abstract List<GroupMember> getGroupMembers();

    public abstract boolean deleteGroupMember(int characterId);

    public abstract boolean deleteGroupMembers(List<GroupMember> groupMembers);

    public abstract InventoryItem getInventoryItem(int inventoryId);

    public abstract List<InventoryItem> getInventoryItems(int characterId);

    public abstract boolean insertInventoryItem(InventoryItem item);

    public abstract boolean insertInventoryItems(List<InventoryItem> items);

    public abstract boolean deleteInventoryItem(int inventoryId);

    public abstract boolean deleteInventoryItems(List<InventoryItem> items);

    public abstract Mail getMail(int mailId);

    public abstract List<Mail> getMails(int characterId);

    public abstract boolean insertMail(Mail mail);

    public abstract boolean insertMails(List<Mail> mails);

    public abstract boolean deleteMail(int mailId);

    public abstract boolean deleteMails(List<Mail> mails);

    public abstract boolean insertSettings(AccountSettings settings);

    public abstract AccountSettings getSettings(int userId);

    public abstract boolean deleteSettings(int userId);

    public abstract boolean insertShopItem(ShopItem item);

    public abstract boolean insertShopItems(List<ShopItem> items);

    public abstract ShopItem getShopItem(int itemId);

    public abstract List<ShopItem> getShopItems();

    public abstract boolean deleteShopItem(int itemId);

    public abstract boolean insertBuddy(SocialEntry buddy);

    public abstract boolean insertBuddies(List<SocialEntry> buddies);

    public abstract List<SocialEntry> getBuddies(int characterId);

    public abstract boolean deleteBuddy(int buddyId);

    public abstract boolean deleteBuddies(List<SocialEntry> buddies);

    public abstract boolean insertIgnore(SocialEntry ignored);

    public abstract List<SocialEntry> getIgnored(int characterId);

    public abstract boolean insertSong(Song song);

    public abstract boolean insertSongs(List<Song> songs);

    public abstract List<Song> getSongs();

    public abstract boolean deleteSong(int songId);

    public abstract boolean insertWeddingRecord(WeddingRecord weddingRecord);

    public abstract boolean insertWeddingRecords(List<WeddingRecord> weddingRecords);

    public abstract List<WeddingRecord> getWeddingRecords();

    public abstract boolean deleteWeddingRecord(int weddingRecordId);

    public abstract boolean deleteWeddingRecords(List<WeddingRecord> weddingRecords);

    public void syncBuddies(int characterId, List<SocialEntry> buddies) {

        List<SocialEntry> dbBuddies = this.getBuddies(characterId);
        List<SocialEntry> deleteBuddies = new ArrayList<>();

        for (SocialEntry dbBuddy : dbBuddies) {
            SocialEntry isBuddy = null;
            for (SocialEntry currentBuddy : buddies) {
                if (dbBuddy.getSocialId() == currentBuddy.getSocialId()) {
                    isBuddy = currentBuddy;
                    break;
                }
            }
            if (isBuddy == null) {
                deleteBuddies.add(dbBuddy);
            }
        }

        this.insertBuddies(buddies);
        this.deleteBuddies(deleteBuddies);
    }

    public void syncInventory(int characterId, List<InventoryItem> items) {

        List<InventoryItem> dbItems = this.getInventoryItems(characterId);
        List<InventoryItem> deleteItems = new ArrayList<>();

        for (InventoryItem dbItem : dbItems) {
            InventoryItem isItem = null;
            for (InventoryItem currentItem : items) {
                if (dbItem.getId() == currentItem.getId()) {
                    isItem = currentItem;
                    break;
                }
            }
            if (isItem == null) {
                deleteItems.add(dbItem);
            }
        }

        this.insertInventoryItems(items);
        this.deleteInventoryItems(deleteItems);
    }

    public void syncMails(int characterId, List<Mail> mails) {

        List<Mail> dbMails = this.getMails(characterId);
        List<Mail> deleteMails = new ArrayList<>();

        for (Mail dbMail : dbMails) {
            Mail isMail = null;
            for (Mail currentMail : mails) {
                if (dbMail.getId() == currentMail.getId()) {
                    isMail = currentMail;
                    break;
                }
            }
            if (isMail == null) {
                deleteMails.add(dbMail);
            }
        }

        this.insertMails(mails);
        this.deleteMails(deleteMails);
    }

    public void syncGroups(List<Group> groups) {

        List<Group> dbGroups = this.getGroups();
        List<Group> deleteGroups = new ArrayList<>();

        for (Group dbGroup : dbGroups) {
            Group isGroup = null;
            for (Group currentGroup : groups) {
                if (dbGroup.getId() == currentGroup.getId()) {
                    isGroup = currentGroup;
                    break;
                }
            }
            if (isGroup == null) {
                deleteGroups.add(dbGroup);
            }
        }

        this.insertGroups(groups);
        this.deleteGroups(deleteGroups);
    }

    public void syncGroupMembers(List<GroupMember> groupMembers) {

        List<GroupMember> dbGroupMembers = this.getGroupMembers();
        List<GroupMember> deleteGroupMembers = new ArrayList<>();

        for (GroupMember dbGroupMember : dbGroupMembers) {
            GroupMember isGroupMember = null;
            for (GroupMember currentGroupMember : groupMembers) {
                if (dbGroupMember.getCharacterId() == currentGroupMember.getCharacterId()) {
                    isGroupMember = currentGroupMember;
                    break;
                }
            }
            if (isGroupMember == null) {
                deleteGroupMembers.add(dbGroupMember);
            }
        }

        this.insertGroupMembers(groupMembers);
        this.deleteGroupMembers(deleteGroupMembers);
    }


    public void syncWeddingRecords(List<WeddingRecord> weddingRecords) {

        List<WeddingRecord> dbWeddingRecords = this.getWeddingRecords();
        List<WeddingRecord> deleteWeddingRecords = new ArrayList<>();

        for (WeddingRecord dbWeddingRecord : dbWeddingRecords) {
            WeddingRecord isWeddingRecord = null;
            for (WeddingRecord currentWeddingRecord : weddingRecords) {
                if (dbWeddingRecord.getId() == currentWeddingRecord.getId()) {
                    isWeddingRecord = currentWeddingRecord;
                    break;
                }
            }
            if (isWeddingRecord == null) {
                deleteWeddingRecords.add(dbWeddingRecord);
            }
        }

        this.insertWeddingRecords(weddingRecords);
        this.deleteWeddingRecords(deleteWeddingRecords);
    }

    /**
     * Creates a new account which will be stored inside the database.
     *
     * @param account      The account name.
     * @param passwordHash The password hash.
     * @return The newly created account.
     */
    public Account registerUser(String account, String passwordHash) {
        Account newAccount = new Account(account, passwordHash, AccountStateType.MEMBER);
        this.insertAccount(newAccount);
        return newAccount;
    }

    /**
     * Changes the password of an existing account.
     *
     * @param accountName     The name of the account where the password should be changed.
     * @param newPasswordHash The new password hash for this account.
     * @return {@code true} on success or {@code false} on failure to change the password hash.
     */
    public boolean changePassword(String accountName, String newPasswordHash) {
        return this.insertPassword(accountName, newPasswordHash);
    }

}
