/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
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

package net.arrowgene.dance.database.maria;

import net.arrowgene.dance.database.Database;
import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountSettings;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MariaDb extends Database {


    private static final Logger logger = LogManager.getLogger(MariaDb.class);
    private MariaDbController controller;
    private MariaDbFactory factory;


    public MariaDb() {
        super();
    }

    @Override
    public boolean insertAccount(Account account) {
        return false;
    }

    @Override
    public boolean insertPassword(String accountName, String newPasswordHash) {
        return false;
    }

    @Override
    public Account getAccount(String accountName) {
        return null;
    }

    @Override
    public Account getAccount(int accountId) {
        return null;
    }

    @Override
    public Account getAccount(String accountName, String passwordHash) {
        return null;
    }

    @Override
    public List<ChannelDetails> getChannels() {
        return null;
    }

    @Override
    public Character getCharacter(String characterName) {
        return null;
    }

    @Override
    public List<Character> getCharactersByUserId(int userId) {
        return null;
    }

    @Override
    public Character getCharacterById(int characterId) {
        return null;
    }

    @Override
    public boolean insertCharacter(Character character) {
        return false;
    }

    @Override
    public boolean insertFavoriteSong(FavoriteSong favoriteSong) {
        return false;
    }

    @Override
    public boolean insertFavoriteSongs(List<FavoriteSong> favoriteSongs) {
        return false;
    }

    @Override
    public List<FavoriteSong> getFavoriteSongs(int characterId) {
        return null;
    }

    @Override
    public boolean deleteFavoriteSong(int favoriteSongId) {
        return false;
    }

    @Override
    public boolean insertGroups(List<Group> groups) {
        return false;
    }

    @Override
    public boolean insertGroup(Group group) {
        return false;
    }

    @Override
    public List<Group> getGroups() {
        return null;
    }

    @Override
    public boolean deleteGroup(int groupId) {
        return false;
    }

    @Override
    public boolean deleteGroups(List<Group> groups) {
        return false;
    }

    @Override
    public boolean insertGroupMember(GroupMember groupMember) {
        return false;
    }

    @Override
    public boolean insertGroupMembers(List<GroupMember> groupMembers) {
        return false;
    }

    @Override
    public List<GroupMember> getGroupMembers(int groupId) {
        return null;
    }

    @Override
    public List<GroupMember> getGroupMembers() {
        return null;
    }

    @Override
    public boolean deleteGroupMember(int characterId) {
        return false;
    }

    @Override
    public boolean deleteGroupMembers(List<GroupMember> groupMembers) {
        return false;
    }

    @Override
    public InventoryItem getInventoryItem(int inventoryId) {
        return null;
    }

    @Override
    public List<InventoryItem> getInventoryItems(int characterId) {
        return null;
    }

    @Override
    public boolean insertInventoryItem(InventoryItem item) {
        return false;
    }

    @Override
    public boolean insertInventoryItems(List<InventoryItem> items) {
        return false;
    }

    @Override
    public boolean deleteInventoryItem(int inventoryId) {
        return false;
    }

    @Override
    public boolean deleteInventoryItems(List<InventoryItem> items) {
        return false;
    }

    @Override
    public Mail getMail(int mailId) {
        return null;
    }

    @Override
    public List<Mail> getMails(int characterId) {
        return null;
    }

    @Override
    public boolean insertMail(Mail mail) {
        return false;
    }

    @Override
    public boolean insertMails(List<Mail> mails) {
        return false;
    }

    @Override
    public boolean deleteMail(int mailId) {
        return false;
    }

    @Override
    public boolean deleteMails(List<Mail> mails) {
        return false;
    }

    @Override
    public boolean insertSettings(AccountSettings settings) {
        return false;
    }

    @Override
    public AccountSettings getSettings(int userId) {
        return null;
    }

    @Override
    public boolean deleteSettings(int userId) {
        return false;
    }

    @Override
    public boolean insertShopItem(ShopItem item) {
        return false;
    }

    @Override
    public boolean insertShopItems(List<ShopItem> items) {
        return false;
    }

    @Override
    public ShopItem getShopItem(int itemId) {
        return null;
    }

    @Override
    public List<ShopItem> getShopItems() {
        return null;
    }

    @Override
    public boolean deleteShopItem(int itemId) {
        return false;
    }

    @Override
    public boolean insertBuddy(SocialEntry buddy) {
        return false;
    }

    @Override
    public boolean insertBuddies(List<SocialEntry> buddies) {
        return false;
    }

    @Override
    public List<SocialEntry> getBuddies(int characterId) {
        return null;
    }

    @Override
    public boolean deleteBuddy(int buddyId) {
        return false;
    }

    @Override
    public boolean deleteBuddies(List<SocialEntry> buddies) {
        return false;
    }

    @Override
    public boolean insertIgnore(SocialEntry ignored) {
        return false;
    }

    @Override
    public List<SocialEntry> getIgnored(int characterId) {
        return null;
    }

    @Override
    public boolean insertSong(Song song) {
        return false;
    }

    @Override
    public boolean insertSongs(List<Song> songs) {
        return false;
    }

    @Override
    public List<Song> getSongs() {
        return null;
    }

    @Override
    public boolean deleteSong(int songId) {
        return false;
    }

    @Override
    public boolean insertWeddingRecord(WeddingRecord weddingRecord) {
        return false;
    }

    @Override
    public boolean insertWeddingRecords(List<WeddingRecord> weddingRecords) {
        return false;
    }

    @Override
    public List<WeddingRecord> getWeddingRecords() {
        return null;
    }

    @Override
    public boolean deleteWeddingRecord(int weddingRecordId) {
        return false;
    }

    @Override
    public boolean deleteWeddingRecords(List<WeddingRecord> weddingRecords) {
        return false;
    }
}
