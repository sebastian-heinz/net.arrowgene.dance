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
import net.arrowgene.dance.database.maria.modules.*;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MariaDb extends Database {

    private static final Logger logger = LogManager.getLogger(MariaDb.class);

    private MariaDbController controller;
    private MariaDbFactory factory;
    private MariaDbAccount account;
    private MariaDbChannel channel;
    private MariaDbCharacter character;
    private MariaDbFavoriteSong favoriteSong;
    private MariaDbGroup group;
    private MariaDbGroupMember groupMember;
    private MariaDbInventoryItem inventoryItem;
    private MariaDbMail mail;
    private MariaDbSettings settings;
    private MariaDbShopItem shopItem;
    private MariaDbSocial social;
    private MariaDbSong song;
    private MariaDbWedding wedding;

    public MariaDb(String host, short port, String database, String user, String password, int timeout, boolean usePool) {
        controller = new MariaDbController(usePool);
        if (!controller.initialize(host, port, database, user, password, timeout)) {
            logger.fatal("Could not initialize MariaDb");
        }
        factory = new MariaDbFactory();
        account = new MariaDbAccount(controller, factory);
        channel = new MariaDbChannel(controller, factory);
        character = new MariaDbCharacter(controller, factory);
        favoriteSong = new MariaDbFavoriteSong(controller, factory);
        group = new MariaDbGroup(controller, factory);
        groupMember = new MariaDbGroupMember(controller, factory);
        inventoryItem = new MariaDbInventoryItem(controller, factory);
        mail = new MariaDbMail(controller, factory);
        settings = new MariaDbSettings(controller, factory);
        shopItem = new MariaDbShopItem(controller, factory);
        social = new MariaDbSocial(controller, factory);
        song = new MariaDbSong(controller, factory);
        wedding = new MariaDbWedding(controller, factory);
    }

    @Override
    public Account getAccount(String accountName) {
        Account account = null;
        try {
            account = this.account.getAccount(accountName);
        } catch (SQLException e) {
            logger.error(e);
        }
        return account;
    }

    @Override
    public Account getAccount(int accountId) {
        Account account = null;
        try {
            account = this.account.getAccount(accountId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return account;
    }

    @Override
    public List<ChannelDetails> getChannels() {
        List<ChannelDetails> channels = null;
        try {
            channels = this.channel.getChannels();
        } catch (SQLException e) {
            logger.error(e);
        }
        return channels;
    }

    @Override
    public Character getCharacter(String characterName) {
        Character character = null;
        try {
            character = this.character.getCharacter(characterName);
        } catch (SQLException e) {
            logger.error(e);
        }
        return character;
    }

    @Override
    public Character getCharacter(int characterId) {
        Character character = null;
        try {
            character = this.character.getCharacter(characterId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return character;
    }

    @Override
    public boolean insertCharacter(Character character) {
        boolean success = true;
        try {
            this.character.insertCharacter(character);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertFavoriteSong(FavoriteSong favoriteSong) {
        boolean success = true;
        try {
            this.favoriteSong.insertFavoriteSong(favoriteSong);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertFavoriteSongs(List<FavoriteSong> favoriteSongs) {
        boolean success = true;
        try {
            this.favoriteSong.insertFavoriteSongs(favoriteSongs);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<FavoriteSong> getFavoriteSongs(int characterId) {
        List<FavoriteSong> favoriteSongs = null;
        try {
            favoriteSongs = this.favoriteSong.getFavoriteSongs(characterId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return favoriteSongs;
    }

    @Override
    public boolean deleteFavoriteSong(int favoriteSongId) {
        boolean success = true;
        try {
            this.favoriteSong.deleteFavoriteSong(favoriteSongId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertGroups(List<Group> groups) {
        boolean success = true;
        try {
            this.group.insertGroups(groups);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<Group> getGroups() {
        List<Group> groups = null;
        try {
            groups = this.group.getGroups();
        } catch (SQLException e) {
            logger.error(e);
        }
        return groups;
    }

    @Override
    public boolean insertGroup(Group group) {
        boolean success = true;
        try {
            this.group.insertGroup(group);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteGroup(int groupId) {
        boolean success = true;
        try {
            this.group.deleteGroup(groupId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteGroups(List<Group> groups) {
        boolean success = true;
        try {
            this.group.deleteGroups(groups);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertGroupMember(GroupMember groupMember) {
        boolean success = true;
        try {
            this.groupMember.insertGroupMember(groupMember);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertGroupMembers(List<GroupMember> groupMembers) {
        boolean success = true;
        try {
            this.groupMember.insertGroupMembers(groupMembers);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<GroupMember> getGroupMembers(int groupId) {
        List<GroupMember> groupMembers = null;
        try {
            groupMembers = this.groupMember.getGroupMembers(groupId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return groupMembers;
    }

    @Override
    public List<GroupMember> getGroupMembers() {
        List<GroupMember> groupMembers = null;
        try {
            groupMembers = this.groupMember.getGroupMembers();
        } catch (SQLException e) {
            logger.error(e);
        }
        return groupMembers;
    }

    @Override
    public boolean deleteGroupMember(int characterId) {
        boolean success = true;
        try {
            this.groupMember.deleteGroupMember(characterId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteGroupMembers(List<GroupMember> groupMembers) {
        boolean success = true;
        try {
            this.groupMember.deleteGroupMembers(groupMembers);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public InventoryItem getInventoryItem(int inventoryId) {
        InventoryItem inventoryItem = null;
        try {
            inventoryItem = this.inventoryItem.getInventoryItem(inventoryId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return inventoryItem;
    }

    @Override
    public List<InventoryItem> getInventoryItems(int characterId) {
        List<InventoryItem> inventoryItems = null;
        try {
            inventoryItems = this.inventoryItem.getInventoryItems(characterId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return inventoryItems;
    }

    @Override
    public boolean insertInventoryItem(InventoryItem item) {
        boolean success = true;
        try {
            this.inventoryItem.insertInventoryItem(item);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertInventoryItems(List<InventoryItem> items) {
        boolean success = true;
        try {
            this.inventoryItem.insertInventoryItems(items);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteInventoryItem(int inventoryId) {
        boolean success = true;
        try {
            this.inventoryItem.deleteInventoryItem(inventoryId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteInventoryItems(List<InventoryItem> items) {
        boolean success = true;
        try {
            this.inventoryItem.deleteInventoryItems(items);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public Mail getMail(int mailId) {
        Mail mail = null;
        try {
            mail = this.mail.getMail(mailId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return mail;
    }

    @Override
    public List<Mail> getMails(int characterId) {
        List<Mail> mails = null;
        try {
            mails = this.mail.getMails(characterId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return mails;
    }

    @Override
    public boolean insertMail(Mail mail) {
        boolean success = true;
        try {
            this.mail.insertMail(mail);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertMails(List<Mail> mails) {
        boolean success = true;
        try {
            this.mail.insertMails(mails);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteMail(int mailId) {
        boolean success = true;
        try {
            this.mail.deleteMail(mailId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteMails(List<Mail> mails) {
        boolean success = true;
        try {
            this.mail.deleteMails(mails);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertSettings(AccountSettings settings) {
        boolean success = true;
        try {
            this.settings.insertSettings(settings);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public AccountSettings getSettings(int userId) {
        AccountSettings settings = null;
        try {
            settings = this.settings.getSettings(userId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return settings;
    }

    @Override
    public boolean deleteSettings(int userId) {
        boolean success = true;
        try {
            this.settings.deleteSettings(userId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertShopItem(ShopItem item) {
        boolean success = true;
        try {
            this.shopItem.insertShopItem(item);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertShopItems(List<ShopItem> items) {
        boolean success = true;
        try {
            this.shopItem.insertShopItems(items);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public ShopItem getShopItem(int itemId) {
        ShopItem shopItem = null;
        try {
            shopItem = this.shopItem.getShopItem(itemId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return shopItem;
    }

    @Override
    public List<ShopItem> getShopItems() {
        List<ShopItem> shopItems = null;
        try {
            shopItems = this.shopItem.getShopItems();
        } catch (SQLException e) {
            logger.error(e);
        }
        return shopItems;
    }

    @Override
    public boolean deleteShopItem(int itemId) {
        boolean success = true;
        try {
            this.shopItem.deleteShopItem(itemId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertBuddy(SocialEntry buddy) {
        boolean success = true;
        try {
            this.social.insertBuddy(buddy);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertBuddies(List<SocialEntry> buddies) {
        boolean success = true;
        try {
            this.social.insertBuddies(buddies);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<SocialEntry> getBuddies(int characterId) {
        List<SocialEntry> buddies = null;
        try {
            buddies = this.social.getBuddies(characterId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return buddies;
    }

    @Override
    public boolean deleteBuddy(int buddyId) {
        boolean success = true;
        try {
            this.social.deleteBuddy(buddyId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteBuddies(List<SocialEntry> buddies) {
        boolean success = true;
        try {
            this.social.deleteBuddies(buddies);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertIgnore(SocialEntry ignored) {
        boolean success = true;
        try {
            this.social.insertIgnore(ignored);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<SocialEntry> getIgnored(int characterId) {
        List<SocialEntry> buddies = null;
        try {
            buddies = this.social.getIgnored(characterId);
        } catch (SQLException e) {
            logger.error(e);
        }
        return buddies;
    }

    @Override
    public boolean insertSong(Song song) {
        boolean success = true;
        try {
            this.song.insertSong(song);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertSongs(List<Song> songs) {
        boolean success = true;
        try {
            this.song.insertSongs(songs);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<Song> getSongs() {
        List<Song> songs = null;
        try {
            songs = this.song.getSongs();
        } catch (SQLException e) {
            logger.error(e);
        }
        return songs;
    }

    @Override
    public boolean deleteSong(int songId) {
        boolean success = true;
        try {
            this.song.deleteSong(songId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertWeddingRecord(WeddingRecord weddingRecord) {
        boolean success = true;
        try {
            this.wedding.insertWeddingRecord(weddingRecord);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertWeddingRecords(List<WeddingRecord> weddingRecords) {
        boolean success = true;
        try {
            this.wedding.insertWeddingRecords(weddingRecords);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<WeddingRecord> getWeddingRecords() {
        List<WeddingRecord> weddingRecords = null;
        try {
            weddingRecords = this.wedding.getWeddingRecords();
        } catch (SQLException e) {
            logger.error(e);
        }
        return weddingRecords;
    }

    @Override
    public boolean deleteWeddingRecord(int weddingRecordId) {
        boolean success = true;
        try {
            this.wedding.deleteWeddingRecord(weddingRecordId);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteWeddingRecords(List<WeddingRecord> weddingRecords) {
        boolean success = true;
        try {
            this.wedding.deleteWeddingRecords(weddingRecords);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public boolean setSetting(String key, String value) {
        try {
            PreparedStatement upsert = controller.createPreparedStatement(
                "INSERT INTO `setting` (`key`, `value`) VALUES (?,?) ON DUPLICATE KEY UPDATE `value`=VALUES(`value`);");
            upsert.setString(1, key);
            upsert.setString(2, value);
            upsert.execute();
            upsert.close();
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    @Override
    public String getSetting(String key) {
        String value = null;
        PreparedStatement select = controller.createPreparedStatement("SELECT `value` FROM `setting` WHERE `key`=?;");
        try {
            select.setString(1, key);
            ResultSet rs = select.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
            rs.close();
            select.close();
        } catch (SQLException e) {
            logger.error(e);
        }

        return value;
    }

}
