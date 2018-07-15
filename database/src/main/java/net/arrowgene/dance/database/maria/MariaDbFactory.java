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

import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountSettings;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.library.models.channel.ChannelDetails;
import net.arrowgene.dance.library.models.channel.ChannelType;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.character.SocialEntry;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.group.GroupRights;
import net.arrowgene.dance.library.models.item.*;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.library.models.mail.MailType;
import net.arrowgene.dance.library.models.mail.SpecialSenderType;
import net.arrowgene.dance.library.models.song.FavoriteSong;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.library.models.song.SongNote;
import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;
import net.arrowgene.dance.library.models.wedding.WeddingState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaDbFactory {

    public Account createAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setUsername(rs.getString("name"));
        account.setPasswordHash(rs.getString("hash"));
        account.setState(AccountStateType.getType(rs.getInt("state")));
        return account;
    }

    public AccountSettings createAccountSettings(ResultSet rs) throws SQLException {
        AccountSettings accountSettings = new AccountSettings(rs.getInt("character_id"));
        accountSettings.setKeyArrowUp(rs.getInt("arrow_up"));
        accountSettings.setKeyArrowDown(rs.getInt("arrow_down"));
        accountSettings.setKeyArrowLeft(rs.getInt("arrow_left"));
        accountSettings.setKeyArrowRight(rs.getInt("arrow_right"));
        accountSettings.setVolBackground(rs.getInt("volume_background"));
        accountSettings.setVolSoundEffect(rs.getInt("volume_sound_effect"));
        accountSettings.setVolGameMusic(rs.getInt("volume_game_music"));
        accountSettings.setNotePanelTransparency(rs.getInt("note_panel_transparency"));
        accountSettings.setVideoSoften(rs.getInt("video_soften"));
        accountSettings.setEffectsScene(rs.getInt("effects_scene"));
        accountSettings.setEffectsAvatar(rs.getInt("effects_avatar"));
        accountSettings.setCameraView(rs.getInt("camera_view"));
        accountSettings.setRate(rs.getInt("rate"));
        return accountSettings;
    }

    public InventoryItem createInventoryItem(ResultSet rs) throws SQLException {
        InventoryItem inventoryItem = new InventoryItem(createShopItem(rs));
        inventoryItem.setId(rs.getInt("id"));
        inventoryItem.setCharacterId(rs.getInt("character_id"));
        inventoryItem.setSlotNumber(rs.getInt("slot_number"));
        inventoryItem.setQuantity(rs.getInt("quantity"));
        inventoryItem.setExpireDate(rs.getInt("expire_date"));
        inventoryItem.setEquipped(rs.getBoolean("is_equipped"));
        return inventoryItem;
    }

    public FavoriteSong createFavoriteSong(ResultSet rs) throws SQLException {
        FavoriteSong favoriteSong = new FavoriteSong();
        favoriteSong.setId(rs.getInt("id"));
        favoriteSong.setSongId(rs.getInt("song_id"));
        favoriteSong.setCharacterId(rs.getInt("character_id"));
        return favoriteSong;
    }

    public ShopItem createShopItem(ResultSet rs) throws SQLException {
        ShopItem shopItem = new ShopItem();
        shopItem.setId(rs.getInt("id"));
        shopItem.setName(rs.getString("name"));
        shopItem.setModelId(rs.getInt("model_id"));
        shopItem.setCategory(ItemCategoryType.getType(rs.getInt("category")));
        shopItem.setMinLevel(rs.getInt("min_level"));
        shopItem.setDuration(ItemDurationType.getType(rs.getInt("duration")));
        shopItem.setPrice(rs.getInt("price"));
        shopItem.setQuantity(ItemQuantityType.getType(rs.getInt("quantity")));
        shopItem.setSex(ItemSexType.getType(rs.getInt("sex")));
        shopItem.setPriceCategory(ItemPriceCategoryType.getType(rs.getInt("price_category")));
        shopItem.setWeddingRing(rs.getBoolean("wedding_ring"));
        return shopItem;
    }

    public Group createGroup(ResultSet rs) throws SQLException {
        Group group = new Group();
        group.setId(rs.getInt("id"));
        group.setName(rs.getString("name"));
        group.setCreationDate(rs.getInt("date"));
        group.setScore(rs.getInt("score"));
        group.setSlogan(rs.getString("slogan"));
        group.setIntroduction(rs.getString("introduction"));
        group.setIcon(rs.getInt("icon"));
        group.setLeaderId(rs.getInt("leader_id"));
        group.setNoticeBoardText(rs.getString("notice_text"));
        group.setNoticeBoardTitle(rs.getString("notice_title"));
        group.setNoticeDate(rs.getInt("notice_date"));
        group.setMaxMembers(rs.getInt("max_members"));
        group.setLeaderName(rs.getString("character_name"));
        return group;
    }

    public GroupMember createGroupMember(ResultSet rs) throws SQLException {
        GroupMember groupMember = new GroupMember();
        groupMember.setCharacterId(rs.getInt("character_id"));
        groupMember.setGroupId(rs.getInt("group_id"));
        groupMember.setJoinDate(rs.getInt("join_date"));
        groupMember.setScore(rs.getInt("score"));
        groupMember.setGroupRights(GroupRights.getType(rs.getInt("rights")));
        groupMember.setCharacterName(rs.getString("character_name"));
        groupMember.setCharacterLevel(rs.getInt("character_level"));
        return groupMember;
    }

    public Character createCharacter(ResultSet rs) throws SQLException {
        Character character = new Character();
        character.setCharacterId(rs.getInt("id"));
        character.setAccountId(rs.getInt("user_id"));
        character.setName(rs.getString("name"));
        character.setLevel(rs.getInt("level"));
        character.setSex(CharacterSexTyp.getType(rs.getInt("sex")));
        character.setFlag(rs.getInt("flag"));
        character.setHair(rs.getInt("hair"));
        character.setGlasses(rs.getInt("glasses"));
        character.setTop(rs.getInt("top"));
        character.setShoes(rs.getInt("shoes"));
        character.setFace(rs.getInt("face"));
        character.setGloves(rs.getInt("gloves"));
        character.setPants(rs.getInt("pants"));
        character.setExperience(rs.getInt("experience"));
        character.setGames(rs.getInt("games"));
        character.setWins(rs.getInt("wins"));
        character.setDraws(rs.getInt("draws"));
        character.setLosses(rs.getInt("losses"));
        character.setHearts(rs.getInt("hearts"));
        character.setMvp(rs.getInt("mvp"));
        character.setPerfects(rs.getInt("perfects"));
        character.setCools(rs.getInt("cools"));
        character.setBads(rs.getInt("bads"));
        character.setMisses(rs.getInt("misses"));
        character.setPoints(rs.getInt("points"));
        character.setCoins(rs.getInt("coins"));
        character.setBonus(rs.getInt("bonus"));
        character.setWeight(rs.getInt("weight"));
        character.setRanking(rs.getInt("ranking"));
        character.setStatusAchieved(rs.getInt("status_achieved"));
        character.setBestScore(rs.getInt("best_score"));
        character.setAge(rs.getInt("age"));
        character.setZodiac(rs.getString("zodiac"));
        character.setCity(rs.getString("city"));
        character.setCalorinsLostWeek(rs.getInt("calorins_lost_week"));
        character.setPointsWon(rs.getInt("points_won"));
        character.setCompetitionWon(rs.getInt("competition_win"));
        character.setCompetitionLost(rs.getInt("competition_lost"));
        character.setMedal(rs.getInt("medal"));
        character.setAllTimeBestRanking(rs.getInt("alltime_best_ranking"));
        character.setTutorial(rs.getInt("tutorial"));
        character.setInfo(rs.getString("info"));
        character.setItemSlotCount(rs.getInt("item_slot_count"));
        character.setClothSlotCount(rs.getInt("cloth_slot_count"));
        return character;
    }

    public Song createSong(ResultSet rs) throws SQLException {
        Song song = new Song();
        song.setSongId(rs.getInt("id"));
        song.setFileId(rs.getInt("file_id"));
        song.setKey(rs.getString("key"));
        song.setName(rs.getString("name"));
        SongNote noteT = new SongNote(rs.getInt("notes_t_size"), rs.getInt("notes_t_crc32"), rs.getInt("notes_t_key"));
        SongNote noteK = new SongNote(rs.getInt("notes_k_size"), rs.getInt("notes_k_crc32"), rs.getInt("notes_k_key"));
        song.setNoteT(noteT);
        song.setNoteK(noteK);
        return song;
    }

    public Mail createMail(ResultSet rs) throws SQLException {
        Mail mail = new Mail();
        mail.setId(rs.getInt("id"));
        mail.setSenderId(rs.getInt("sender_id"));
        mail.setReceiverId(rs.getInt("receiver_id"));
        mail.setGiftItemId(rs.getInt("gift_item_id"));
        mail.setDate(rs.getLong("date"));
        mail.setBody(rs.getString("body"));
        mail.setSubject(rs.getString("subject"));
        mail.setType(MailType.getType(rs.getByte("type")));
        mail.setRead(rs.getBoolean("read"));
        mail.setSpecialSender(SpecialSenderType.getType(rs.getByte("special_sender")));
        mail.setSenderCharacterName(rs.getString("sender_character_name"));
        return mail;
    }

    public ChannelDetails createChannel(ResultSet rs) throws SQLException {
        ChannelDetails channel = new ChannelDetails();
        channel.setName(rs.getString("name"));
        channel.setType(ChannelType.getType(rs.getInt("typ")));
        channel.setPosition(rs.getInt("position"));
        return channel;
    }

    public SocialEntry createSocialEntry(ResultSet rs) throws SQLException {
        SocialEntry socialEntry = new SocialEntry();
        socialEntry.setId(rs.getInt("id"));
        socialEntry.setCharacterId(rs.getInt("my_character_id"));
        socialEntry.setSocialId(rs.getInt("character_id"));
       //socialEntry.setSocialName(rs.getString("character_name"));
        return socialEntry;
    }

    public WeddingRecord createWeddingRecord(ResultSet rs) throws SQLException {
        WeddingRecord weddingRecord = new WeddingRecord();
        weddingRecord.setId(rs.getInt("id"));
        weddingRecord.setGroomId(rs.getInt("groom_id"));
        weddingRecord.setBrideId(rs.getInt("bride_id"));
        weddingRecord.setMarriedDate(rs.getLong("married_date"));
        weddingRecord.setDivorceDate(rs.getLong("divorce_date"));
        weddingRecord.setEngageDate(rs.getLong("engage_date"));
        weddingRecord.setGroomState(WeddingState.getType(rs.getByte("groom_state")));
        weddingRecord.setBrideState(WeddingState.getType(rs.getByte("bride_state")));
        weddingRecord.setRingType(RingType.getType(rs.getByte("ring")));
        weddingRecord.setGroomCharacterName(rs.getString("groom_character_name"));
        weddingRecord.setBrideCharacterName(rs.getString("bride_character_name"));
        return weddingRecord;
    }
}
