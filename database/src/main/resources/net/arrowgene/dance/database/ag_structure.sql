PRAGMA foreign_keys = on;

CREATE TABLE IF NOT EXISTS `ag_user` (
  `user_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `user_account` VARCHAR(45) NOT NULL CONSTRAINT uq_user_account UNIQUE,
  `user_password` VARCHAR(45) NOT NULL,
  `user_state` INTEGER NOT NULL,
  `user_active_character_id` INTEGER DEFAULT NULL REFERENCES ag_character (character_id)
);

CREATE TABLE IF NOT EXISTS `ag_group` (
  `group_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `group_name` VARCHAR(255) NOT NULL,
  `group_date` INTEGER NOT NULL,
  `group_score` INTEGER NOT NULL,
  `group_slogan` VARCHAR(255) NOT NULL,
  `group_introduction` VARCHAR(255) NOT NULL,
  `group_icon` INTEGER NOT NULL,
  `group_leader_id` INTEGER (11) NOT NULL REFERENCES ag_character (character_id),
  `group_notice_text` VARCHAR(255) NOT NULL,
  `group_notice_title` VARCHAR(255) NOT NULL,
  `group_notice_date` INTEGER NOT NULL,
  `group_max_members` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_item` (
    `item_id` INTEGER PRIMARY KEY,
    `item_name` VARCHAR(255) NOT NULL,
    `item_model_id` INTEGER NOT NULL,
    `item_category` INTEGER NOT NULL,
    `item_min_level` INTEGER NOT NULL,
    `item_duration` INTEGER NOT NULL,
    `item_price` INTEGER NOT NULL,
    `item_quantity` INTEGER NOT NULL,
    `item_sex` INTEGER NOT NULL,
    `item_price_category` INTEGER NOT NULL,
    `item_wedding_ring` BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_wedding` (
    `wedding_id` INTEGER PRIMARY KEY,
    `wedding_groom_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
    `wedding_bride_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
    `wedding_married_date` INTEGER NOT NULL,
    `wedding_divorce_date` INTEGER NOT NULL,
    `wedding_engage_date` INTEGER NOT NULL,
    `wedding_groom_state` TINYINT(1) NOT NULL,
    `wedding_bride_state` TINYINT(1) NOT NULL,
    `wedding_ring` TINYINT(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_character` (
  `character_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `user_id` INTEGER REFERENCES `ag_user` (user_id) NOT NULL,
  `character_name` VARCHAR(45) NOT NULL,
  `character_level` INTEGER NOT NULL,
  `character_sex` INTEGER NOT NULL,
  `character_flag` INTEGER NOT NULL,
  `character_hair` INTEGER NOT NULL,
  `character_glasses` INTEGER NOT NULL,
  `character_top` INTEGER NOT NULL,
  `character_shoes` INTEGER NOT NULL,
  `character_face` INTEGER NOT NULL,
  `character_gloves` INTEGER NOT NULL,
  `character_pants` INTEGER NOT NULL,
  `character_experience` INTEGER NOT NULL,
  `character_games` INTEGER NOT NULL,
  `character_wins` INTEGER NOT NULL,
  `character_draws` INTEGER NOT NULL,
  `character_losses` INTEGER NOT NULL,
  `character_hearts` INTEGER NOT NULL,
  `character_mvp` INTEGER NOT NULL,
  `character_perfects` INTEGER NOT NULL,
  `character_cools` INTEGER NOT NULL,
  `character_bads` INTEGER NOT NULL,
  `character_misses` INTEGER NOT NULL,
  `character_points` INTEGER NOT NULL,
  `character_coins` INTEGER NOT NULL,
  `character_bonus` INTEGER NOT NULL,
  `character_weight` INTEGER NOT NULL,
  `character_ranking` INTEGER NOT NULL,
  `character_status_achieved` INTEGER NOT NULL,
  `character_best_score` INTEGER NOT NULL,
  `character_age` INTEGER NOT NULL,
  `character_zodiac` VARCHAR(11) NOT NULL,
  `character_city` VARCHAR(11) NOT NULL,
  `character_calorins_lost_week` INTEGER NOT NULL,
  `character_points_won` INTEGER NOT NULL,
  `character_competition_win` INTEGER NOT NULL,
  `character_competition_lost` INTEGER NOT NULL,
  `character_medal` INTEGER NOT NULL,
  `character_alltime_best_ranking` INTEGER NOT NULL,
  `character_tutorial` INTEGER NOT NULL,
  `character_info` VARCHAR(255) NOT NULL,
  `character_item_slot_count` INTEGER NOT NULL,
  `character_cloth_slot_count` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_inventory` (
  `inventory_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `character_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `item_id` INTEGER REFERENCES ag_item (item_id) NOT NULL,
  `inventory_slot_number` INTEGER NOT NULL,
  `inventory_quantity` INTEGER NOT NULL,
  `inventory_expire_date` INTEGER NOT NULL,
  `inventory_is_equipped` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_group_member` (
  `character_id` INTEGER PRIMARY KEY REFERENCES ag_character (character_id) NOT NULL,
  `group_id` INTEGER REFERENCES ag_group (group_id) NOT NULL,
  `group_member_join_date` INTEGER NOT NULL,
  `group_member_score` INTEGER NOT NULL,
  `group_member_rights` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_channel` (
  `channel_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `channel_typ` INTEGER NOT NULL,
  `channel_position` INTEGER NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_friend` (
  `friend_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `character_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `friend_character_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `friend_character_name` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_ignore` (
  `ignore_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `character_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `ignore_character_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `ignore_character_name` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_mail` (
  `mail_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `mail_sender_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `mail_receiver_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  `mail_gift_item_id` INTEGER NOT NULL,
  `mail_date` INTEGER NOT NULL,
  `mail_body` TEXT NOT NULL,
  `mail_subject` VARCHAR(255) NOT NULL,
  `mail_type` TINYINT(1) NOT NULL,
  `mail_read` BIT NOT NULL,
  `mail_special_sender` TINYINT(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_settings` (
  `user_id` INTEGER REFERENCES `ag_user` (user_id) PRIMARY KEY NOT NULL,
  `settings_arrow_left` INTEGER NOT NULL,
  `settings_arrow_up` INTEGER NOT NULL,
  `settings_arrow_down` INTEGER NOT NULL,
  `settings_arrow_right` INTEGER NOT NULL,
  `settings_volume_background` INTEGER NOT NULL,
  `settings_volume_sound_effect` INTEGER NOT NULL,
  `settings_volume_game_music` INTEGER NOT NULL,
  `settings_note_panel_transparency` INTEGER NOT NULL,
  `settings_video_soften` INTEGER NOT NULL,
  `settings_effects_scene` INTEGER NOT NULL,
  `settings_effects_avatar` INTEGER NOT NULL,
  `settings_camera_view` INTEGER NOT NULL,
  `settings_rate` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_song` (
  `song_id` INTEGER PRIMARY KEY,
  `song_file_id` INTEGER NOT NULL,
  `song_name` VARCHAR(255) NOT NULL,
  `song_key` TEXT NOT NULL,
  `notes_k_crc32` INTEGER NOT NULL,
  `notes_k_key` INTEGER NOT NULL,
  `notes_k_size` INTEGER NOT NULL,
  `notes_t_crc32` INTEGER NOT NULL,
  `notes_t_key` INTEGER NOT NULL,
  `notes_t_size` INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS `ag_favorite_song` (
  `favorite_song_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `song_id` INTEGER NOT NULL REFERENCES ag_song (song_id),
  `character_id` INTEGER NOT NULL REFERENCES ag_character (character_id),
  CONSTRAINT uq_songId_characterId UNIQUE (song_id, character_id) ON CONFLICT ABORT
);
