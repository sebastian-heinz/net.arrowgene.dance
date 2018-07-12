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

package net.arrowgene.dance.database.sqlite;

import net.arrowgene.dance.database.Database;
import net.arrowgene.dance.database.sqlite.modules.*;
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

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SQLiteDb extends Database {

    private static final Logger logger = LogManager.getLogger(SQLiteDb.class);

    private SQLiteController controller;
    private SQLiteFactory factory;
    private SQLiteAccount account;
    private SQLiteChannel channel;
    private SQLiteCharacter character;
    private SQLiteFavoriteSong favoriteSong;
    private SQLiteGroup group;
    private SQLiteGroupMember groupMember;
    private SQLiteInventoryItem inventoryItem;
    private SQLiteMail mail;
    private SQLiteSettings settings;
    private SQLiteShopItem shopItem;
    private SQLiteSocial social;
    private SQLiteSong song;
    private SQLiteWedding wedding;

    public SQLiteDb() {
        super();

        this.factory = new SQLiteFactory();

        boolean initDB = false;
        File dbFile = new File("db.db3");
        if (!dbFile.exists()) {
            initDB = true;
        }

        controller = new SQLiteController("db.db3");
        controller.initialize();

        if (initDB) {
            initDatabase();
        }

        this.account = new SQLiteAccount(this.controller, this.factory);
        this.channel = new SQLiteChannel(this.controller, this.factory);
        this.character = new SQLiteCharacter(this.controller, this.factory);
        this.favoriteSong = new SQLiteFavoriteSong(this.controller, this.factory);
        this.group = new SQLiteGroup(this.controller, this.factory);
        this.groupMember = new SQLiteGroupMember(this.controller, this.factory);
        this.inventoryItem = new SQLiteInventoryItem(this.controller, this.factory);
        this.mail = new SQLiteMail(this.controller, this.factory);
        this.settings = new SQLiteSettings(this.controller, this.factory);
        this.shopItem = new SQLiteShopItem(this.controller, this.factory);
        this.social = new SQLiteSocial(this.controller, this.factory);
        this.song = new SQLiteSong(this.controller, this.factory);
        this.wedding = new SQLiteWedding(this.controller, this.factory);
    }

    private void initDatabase() {
        logger.info("initialize SQLiteDb");

        importFile("ag_structure.sql");
        importFile("ag_items.sql");
        importFile("ag_songs.sql");
        importFile("ag_default.sql");
    }

    private void importFile(String fileName) {

        // Check if file is present
        InputStream sqlStructureFile = Database.class.getResourceAsStream(fileName);
        if (sqlStructureFile != null) {
            super.logger.writeDebug("Import file " + fileName);
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(sqlStructureFile));

                String sCurrentLine;
                while ((sCurrentLine = bfReader.readLine()) != null) {
                    sb.append(sCurrentLine + "\n");
                }

                String[] strSQL = sb.toString().split(";");
                for (int i = 0; i < strSQL.length; i++) {
                    if (!strSQL[i].trim().equals("")) {
                        executeSQL(strSQL[i] + ";");
                    }
                }
            } catch (FileNotFoundException e) {
                super.logger.writeLog(e);
            } catch (IOException e) {
                super.logger.writeLog(e);
            }
        } else {
            System.out.println("Could not find file " + fileName);
        }
    }

    private void executeSQL(String sql) {
        Statement stmt = controller.createStatement();

        try {
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            super.logger.writeLog(e);
        }
    }

    public int getAutoInc(Statement stmt) throws SQLException {
        int inc = 0;
        ResultSet autoInc = stmt.getGeneratedKeys();
        inc = autoInc.getInt(1);
        autoInc.close();
        return inc;
    }

    @Override
    public boolean insertAccount(Account account) {
        boolean success = true;
        try {
            this.account.insertAccount(account);
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean insertPassword(String accountName, String newPasswordHash) {
        boolean success = true;
        try {
            this.account.insertPassword(accountName, newPasswordHash);
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public Account getAccount(String accountName) {
        Account account = null;
        try {
            account = this.account.getAccount(accountName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public Account getAccount(int accountId) {
        Account account = null;
        try {
            account = this.account.getAccount(accountId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public Account getAccount(String accountName, String passwordHash) {
        Account account = null;
        try {
            account = this.account.getAccount(accountName, passwordHash);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public List<ChannelDetails> getChannels() {
        List<ChannelDetails> channels = null;
        try {
            channels = this.channel.getChannels();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    @Override
    public Character getCharacter(String characterName) {
        Character character = null;
        try {
            character = this.character.getCharacter(characterName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return character;
    }

    @Override
    public List<Character> getCharactersByUserId(int userId) {
        List<Character> characters = null;
        try {
            characters = this.character.getCharactersByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return characters;
    }

    @Override
    public Character getCharacterById(int characterId) {
        Character character = null;
        try {
            character = this.character.getCharacterById(characterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return character;
    }

    @Override
    public boolean insertCharacter(Character character) {
        boolean success = true;
        try {
            this.character.insertCharacter(character);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return favoriteSongs;
    }

    @Override
    public boolean deleteFavoriteSong(int favoriteSongId) {
        boolean success = true;
        try {
            this.favoriteSong.deleteFavoriteSong(favoriteSongId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public boolean insertGroup(Group group) {
        boolean success = true;
        try {
            this.group.insertGroup(group);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return groupMembers;
    }

    @Override
    public List<GroupMember> getGroupMembers() {
        List<GroupMember> groupMembers = null;
        try {
            groupMembers = this.groupMember.getGroupMembers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupMembers;
    }

    @Override
    public boolean deleteGroupMember(int characterId) {
        boolean success = true;
        try {
            this.groupMember.deleteGroupMember(characterId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return inventoryItem;
    }

    @Override
    public List<InventoryItem> getInventoryItems(int characterId) {
        List<InventoryItem> inventoryItems = null;
        try {
            inventoryItems = this.inventoryItem.getInventoryItems(characterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryItems;
    }

    @Override
    public boolean insertInventoryItem(InventoryItem item) {
        boolean success = true;
        try {
            this.inventoryItem.insertInventoryItem(item);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return mail;
    }

    @Override
    public List<Mail> getMails(int characterId) {
        List<Mail> mails = null;
        try {
            mails = this.mail.getMails(characterId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mails;
    }

    @Override
    public boolean insertMail(Mail mail) {
        boolean success = true;
        try {
            this.mail.insertMail(mail);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return settings;
    }

    @Override
    public boolean deleteSettings(int userId) {
        boolean success = true;
        try {
            this.settings.deleteSettings(userId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return shopItem;
    }

    @Override
    public List<ShopItem> getShopItems() {
        List<ShopItem> shopItems = null;
        try {
            shopItems = this.shopItem.getShopItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shopItems;
    }

    @Override
    public boolean deleteShopItem(int itemId) {
        boolean success = true;
        try {
            this.shopItem.deleteShopItem(itemId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return buddies;
    }

    @Override
    public boolean deleteBuddy(int buddyId) {
        boolean success = true;
        try {
            this.social.deleteBuddy(buddyId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return buddies;
    }

    @Override
    public boolean insertSong(Song song) {
        boolean success = true;
        try {
            this.song.insertSong(song);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    public boolean deleteSong(int songId) {
        boolean success = true;
        try {
            this.song.deleteSong(songId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return weddingRecords;
    }

    @Override
    public boolean deleteWeddingRecord(int weddingRecordId) {
        boolean success = true;
        try {
            this.wedding.deleteWeddingRecord(weddingRecordId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
