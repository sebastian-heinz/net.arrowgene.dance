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

import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountSettings;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.library.models.channel.ChannelDetails;
import net.arrowgene.dance.library.models.channel.ChannelType;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.character.SocialEntry;
import net.arrowgene.dance.library.models.mail.SpecialSenderType;
import net.arrowgene.dance.library.models.wedding.WeddingState;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.group.GroupRights;
import net.arrowgene.dance.library.models.item.*;
import net.arrowgene.dance.library.models.mail.MailType;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.library.models.song.FavoriteSong;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.library.models.song.SongNote;
import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteFactory {

    public Account createAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("user_id"));
        account.setUsername(rs.getString("user_account"));
        account.setPasswordHash(rs.getString("user_password"));
        account.setState(AccountStateType.getType(rs.getInt("user_state")));
        int activeCharacterId = rs.getInt("user_active_character_id");
        if (rs.wasNull()) {
            account.setActiveCharacterId(-1);
        } else {
            account.setActiveCharacterId(activeCharacterId);
        }
        return account;
    }

    public AccountSettings createAccountSettings(ResultSet rs) throws SQLException {
        AccountSettings accountSettings = new AccountSettings(rs.getInt("user_id"));
        accountSettings.setKeyArrowUp(rs.getInt("settings_arrow_up"));
        accountSettings.setKeyArrowDown(rs.getInt("settings_arrow_down"));
        accountSettings.setKeyArrowLeft(rs.getInt("settings_arrow_left"));
        accountSettings.setKeyArrowRight(rs.getInt("settings_arrow_right"));
        accountSettings.setVolBackground(rs.getInt("settings_volume_background"));
        accountSettings.setVolSoundEffect(rs.getInt("settings_volume_sound_effect"));
        accountSettings.setVolGameMusic(rs.getInt("settings_volume_game_music"));
        accountSettings.setNotePanelTransparency(rs.getInt("settings_note_panel_transparency"));
        accountSettings.setVideoSoften(rs.getInt("settings_video_soften"));
        accountSettings.setEffectsScene(rs.getInt("settings_effects_scene"));
        accountSettings.setEffectsAvatar(rs.getInt("settings_effects_avatar"));
        accountSettings.setCameraView(rs.getInt("settings_camera_view"));
        accountSettings.setRate(rs.getInt("settings_rate"));
        return accountSettings;
    }

    public InventoryItem createInventoryItem(ResultSet rs) throws SQLException {
        InventoryItem inventoryItem = new InventoryItem(this.createShopItem(rs));
        inventoryItem.setId(rs.getInt("inventory_id"));
        inventoryItem.setCharacterId(rs.getInt("character_id"));
        inventoryItem.setSlotNumber(rs.getInt("inventory_slot_number"));
        inventoryItem.setQuantity(rs.getInt("inventory_quantity"));
        inventoryItem.setExpireDate(rs.getInt("inventory_expire_date"));
        inventoryItem.setEquipped(rs.getBoolean("inventory_is_equipped"));
        return inventoryItem;
    }

    public FavoriteSong createFavoriteSong(ResultSet rs) throws SQLException {
        FavoriteSong favoriteSong = new FavoriteSong();
        favoriteSong.setId(rs.getInt("favorite_song_id"));
        favoriteSong.setSongId(rs.getInt("song_id"));
        favoriteSong.setCharacterId(rs.getInt("character_id"));
        return favoriteSong;
    }

    public ShopItem createShopItem(ResultSet rs) throws SQLException {
        ShopItem shopItem = new ShopItem();
        shopItem.setId(rs.getInt("item_id"));
        shopItem.setName(rs.getString("item_name"));
        shopItem.setModelId(rs.getInt("item_model_id"));
        shopItem.setCategory(ItemCategoryType.getType(rs.getInt("item_category")));
        shopItem.setMinLevel(rs.getInt("item_min_level"));
        shopItem.setDuration(ItemDurationType.getType(rs.getInt("item_duration")));
        shopItem.setPrice(rs.getInt("item_price"));
        shopItem.setQuantity(ItemQuantityType.getType(rs.getInt("item_quantity")));
        shopItem.setSex(ItemSexType.getType(rs.getInt("item_sex")));
        shopItem.setPriceCategory(ItemPriceCategoryType.getType(rs.getInt("item_price_category")));
        shopItem.setWeddingRing(rs.getBoolean("item_wedding_ring"));
        return shopItem;
    }

    public Group createGroup(ResultSet rs) throws SQLException {
        Group group = new Group();
        group.setId(rs.getInt("group_id"));
        group.setName(rs.getString("group_name"));
        group.setCreationDate(rs.getInt("group_date"));
        group.setScore(rs.getInt("group_score"));
        group.setSlogan(rs.getString("group_slogan"));
        group.setIntroduction(rs.getString("group_introduction"));
        group.setIcon(rs.getInt("group_icon"));
        group.setLeaderId(rs.getInt("group_leader_id"));
        group.setNoticeBoardText(rs.getString("group_notice_text"));
        group.setNoticeBoardTitle(rs.getString("group_notice_title"));
        group.setNoticeDate(rs.getInt("group_notice_date"));
        group.setMaxMembers(rs.getInt("group_max_members"));
        group.setLeaderName(rs.getString("character_name"));
        return group;
    }

    public GroupMember createGroupMember(ResultSet rs) throws SQLException {
        GroupMember groupMember = new GroupMember();
        groupMember.setCharacterId(rs.getInt("character_id"));
        groupMember.setGroupId(rs.getInt("group_id"));
        groupMember.setJoinDate(rs.getInt("group_member_join_date"));
        groupMember.setScore(rs.getInt("group_member_score"));
        groupMember.setGroupRights(GroupRights.getType(rs.getInt("group_member_rights")));
        groupMember.setCharacterName(rs.getString("character_name"));
        groupMember.setCharacterLevel(rs.getInt("character_level"));
        return groupMember;
    }

    public Character createCharacter(ResultSet rs) throws SQLException {
        Character character = new Character();
        character.setCharacterId(rs.getInt("character_id"));
        character.setAccountId(rs.getInt("user_id"));
        character.setName(rs.getString("character_name"));
        character.setLevel(rs.getInt("character_level"));
        character.setSex(CharacterSexTyp.getType(rs.getInt("character_sex")));
        character.setFlag(rs.getInt("character_flag"));
        character.setHair(rs.getInt("character_hair"));
        character.setGlasses(rs.getInt("character_glasses"));
        character.setTop(rs.getInt("character_top"));
        character.setShoes(rs.getInt("character_shoes"));
        character.setFace(rs.getInt("character_face"));
        character.setGloves(rs.getInt("character_gloves"));
        character.setPants(rs.getInt("character_pants"));
        character.setExperience(rs.getInt("character_experience"));
        character.setGames(rs.getInt("character_games"));
        character.setWins(rs.getInt("character_wins"));
        character.setDraws(rs.getInt("character_draws"));
        character.setLosses(rs.getInt("character_losses"));
        character.setHearts(rs.getInt("character_hearts"));
        character.setMvp(rs.getInt("character_mvp"));
        character.setPerfects(rs.getInt("character_perfects"));
        character.setCools(rs.getInt("character_cools"));
        character.setBads(rs.getInt("character_bads"));
        character.setMisses(rs.getInt("character_misses"));
        character.setPoints(rs.getInt("character_points"));
        character.setCoins(rs.getInt("character_coins"));
        character.setBonus(rs.getInt("character_bonus"));
        character.setWeight(rs.getInt("character_weight"));
        character.setRanking(rs.getInt("character_ranking"));
        character.setStatusAchieved(rs.getInt("character_status_achieved"));
        character.setBestScore(rs.getInt("character_best_score"));
        character.setAge(rs.getInt("character_age"));
        character.setZodiac(rs.getString("character_zodiac"));
        character.setCity(rs.getString("character_city"));
        character.setCalorinsLostWeek(rs.getInt("character_calorins_lost_week"));
        character.setPointsWon(rs.getInt("character_points_won"));
        character.setCompetitionWon(rs.getInt("character_competition_win"));
        character.setCompetitionLost(rs.getInt("character_competition_lost"));
        character.setMedal(rs.getInt("character_medal"));
        character.setAllTimeBestRanking(rs.getInt("character_alltime_best_ranking"));
        character.setTutorial(rs.getInt("character_tutorial"));
        character.setInfo(rs.getString("character_info"));
        character.setItemSlotCount(rs.getInt("character_item_slot_count"));
        character.setClothSlotCount(rs.getInt("character_cloth_slot_count"));
        return character;
    }

    public Song createSong(ResultSet rs) throws SQLException {
        Song song = new Song();

        song.setSongId(rs.getInt("song_id"));
        song.setFileId(rs.getInt("song_file_id"));
        song.setKey(rs.getString("song_key"));
        song.setName(rs.getString("song_name"));

        SongNote noteT = new SongNote(rs.getInt("notes_t_size"), rs.getInt("notes_t_crc32"), rs.getInt("notes_t_key"));

        SongNote noteK = new SongNote(rs.getInt("notes_k_size"), rs.getInt("notes_k_crc32"), rs.getInt("notes_k_key"));

        song.setNoteT(noteT);
        song.setNoteK(noteK);

        return song;
    }

    public Mail createMail(ResultSet rs) throws SQLException {
        Mail mail = new Mail();

        mail.setId(rs.getInt("mail_id"));
        mail.setSenderId(rs.getInt("mail_sender_id"));
        mail.setReceiverId(rs.getInt("mail_receiver_id"));
        mail.setGiftItemId(rs.getInt("mail_gift_item_id"));
        mail.setDate(rs.getLong("mail_date"));
        mail.setBody(rs.getString("mail_body"));
        mail.setSubject(rs.getString("mail_subject"));
        mail.setType(MailType.getType(rs.getByte("mail_type")));
        mail.setRead(rs.getBoolean("mail_read"));
        mail.setSpecialSender(SpecialSenderType.getType(rs.getByte("mail_special_sender")));
        mail.setSenderCharacterName(rs.getString("sender_character_name"));
        return mail;
    }

    public ChannelDetails createChannel(ResultSet rs) throws SQLException {
        ChannelDetails channel = new ChannelDetails();

        channel.setName(rs.getString("channel_name"));
        channel.setType(ChannelType.getType(rs.getInt("channel_typ")));
        channel.setPosition(rs.getInt("channel_position"));

        return channel;
    }

    public SocialEntry createSocialEntry(ResultSet rs) throws SQLException {
        SocialEntry socialEntry = new SocialEntry();

        socialEntry.setId(rs.getInt("friend_id"));
        socialEntry.setCharacterId(rs.getInt("character_id"));
        socialEntry.setSocialId(rs.getInt("friend_character_id"));
        socialEntry.setSocialName(rs.getString("friend_character_name"));

        return socialEntry;
    }

    public WeddingRecord createWeddingRecord(ResultSet rs) throws SQLException {
        WeddingRecord weddingRecord = new WeddingRecord();
        weddingRecord.setId(rs.getInt("wedding_id"));
        weddingRecord.setGroomId(rs.getInt("wedding_groom_id"));
        weddingRecord.setBrideId(rs.getInt("wedding_bride_id"));
        weddingRecord.setMarriedDate(rs.getLong("wedding_married_date"));
        weddingRecord.setDivorceDate(rs.getLong("wedding_divorce_date"));
        weddingRecord.setEngageDate(rs.getLong("wedding_engage_date"));
        weddingRecord.setGroomState(WeddingState.getType(rs.getByte("wedding_groom_state")));
        weddingRecord.setBrideState(WeddingState.getType(rs.getByte("wedding_bride_state")));
        weddingRecord.setRingType(RingType.getType(rs.getByte("wedding_ring")));
        weddingRecord.setGroomCharacterName(rs.getString("groom_character_name"));
        weddingRecord.setBrideCharacterName(rs.getString("bride_character_name"));
        return weddingRecord;
    }
}
