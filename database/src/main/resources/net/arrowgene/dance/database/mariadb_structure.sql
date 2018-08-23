CREATE TABLE IF NOT EXISTS `setting` (
  `key`   VARCHAR(255) NOT NULL,
  `value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`key`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `account` (
  `id`               INT(11)      NOT NULL AUTO_INCREMENT,
  `name`             VARCHAR(17)  NOT NULL,
  `normal_name`      VARCHAR(17)  NOT NULL,
  `hash`             VARCHAR(255) NOT NULL,
  `mail`             VARCHAR(255) NOT NULL,
  `mail_verified`    TINYINT(1)   NOT NULL,
  `mail_verified_at` DATETIME              DEFAULT NULL,
  `mail_token`       VARCHAR(255)          DEFAULT NULL,
  `password_token`   VARCHAR(255)          DEFAULT NULL,
  `state`            INT(11)      NOT NULL,
  `last_login`       DATETIME              DEFAULT NULL,
  `created`          DATETIME     NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_account_name` (`name`),
  UNIQUE KEY `uq_account_normal_name` (`normal_name`),
  UNIQUE KEY `uq_account_mail` (`mail`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_channel` (
  `id`       INT(11)     NOT NULL AUTO_INCREMENT,
  `typ`      INT(11)     NOT NULL,
  `position` INT(11)     NOT NULL,
  `name`     VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_channel_name` (`name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_character` (
  `id`                   INT(11)      NOT NULL,
  `name`                 VARCHAR(45)  NOT NULL,
  `level`                INT(11)      NOT NULL,
  `sex`                  INT(11)      NOT NULL,
  `flag`                 INT(11)      NOT NULL,
  `hair`                 INT(11)      NOT NULL,
  `glasses`              INT(11)      NOT NULL,
  `top`                  INT(11)      NOT NULL,
  `shoes`                INT(11)      NOT NULL,
  `face`                 INT(11)      NOT NULL,
  `gloves`               INT(11)      NOT NULL,
  `pants`                INT(11)      NOT NULL,
  `experience`           INT(11)      NOT NULL,
  `games`                INT(11)      NOT NULL,
  `wins`                 INT(11)      NOT NULL,
  `draws`                INT(11)      NOT NULL,
  `losses`               INT(11)      NOT NULL,
  `hearts`               INT(11)      NOT NULL,
  `mvp`                  INT(11)      NOT NULL,
  `perfects`             INT(11)      NOT NULL,
  `cools`                INT(11)      NOT NULL,
  `bads`                 INT(11)      NOT NULL,
  `misses`               INT(11)      NOT NULL,
  `points`               INT(11)      NOT NULL,
  `coins`                INT(11)      NOT NULL,
  `bonus`                INT(11)      NOT NULL,
  `weight`               INT(11)      NOT NULL,
  `ranking`              INT(11)      NOT NULL,
  `status_achieved`      INT(11)      NOT NULL,
  `best_score`           INT(11)      NOT NULL,
  `age`                  INT(11)      NOT NULL,
  `zodiac`               VARCHAR(11)  NOT NULL,
  `city`                 VARCHAR(11)  NOT NULL,
  `calorins_lost_week`   INT(11)      NOT NULL,
  `points_won`           INT(11)      NOT NULL,
  `competition_win`      INT(11)      NOT NULL,
  `competition_lost`     INT(11)      NOT NULL,
  `medal`                INT(11)      NOT NULL,
  `alltime_best_ranking` INT(11)      NOT NULL,
  `tutorial`             INT(11)      NOT NULL,
  `info`                 VARCHAR(255) NOT NULL,
  `item_slot_count`      INT(11)      NOT NULL,
  `cloth_slot_count`     INT(11)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_character_name` (`name`),
  CONSTRAINT `fk_dance_character_id` FOREIGN KEY (`id`) REFERENCES `account` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_friend` (
  `id`              INT(11) NOT NULL AUTO_INCREMENT,
  `my_character_id` INT(11) NOT NULL,
  `character_id`    INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_friend_my_character_id` (`my_character_id`),
  KEY `fk_dance_friend_character_id` (`character_id`),
  CONSTRAINT `fk_dance_friend_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_friend_my_character_id` FOREIGN KEY (`my_character_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_group` (
  `id`           INT(11)      NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(255) NOT NULL,
  `date`         INT(11)      NOT NULL,
  `score`        INT(11)      NOT NULL,
  `slogan`       VARCHAR(255) NOT NULL,
  `introduction` VARCHAR(255) NOT NULL,
  `icon`         INT(11)      NOT NULL,
  `leader_id`    INT(11)      NOT NULL,
  `notice_text`  VARCHAR(255) NOT NULL,
  `notice_title` VARCHAR(255) NOT NULL,
  `notice_date`  INT(11)      NOT NULL,
  `max_members`  INT(11)      NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_group_name` (`name`),
  KEY `fk_dance_group_leader_id` (`leader_id`),
  CONSTRAINT `fk_dance_group_leader_id` FOREIGN KEY (`leader_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_group_member` (
  `character_id` INT(11) NOT NULL,
  `group_id`     INT(11) NOT NULL,
  `join_date`    INT(11) NOT NULL,
  `score`        INT(11) NOT NULL,
  `rights`       INT(11) NOT NULL,
  KEY `fk_dance_group_member_character_id` (`character_id`),
  KEY `fk_dance_group_member_group_id` (`group_id`),
  CONSTRAINT `fk_dance_group_member_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_group_member_group_id` FOREIGN KEY (`group_id`) REFERENCES `dance_group` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_ignore` (
  `id`              INT(11) NOT NULL AUTO_INCREMENT,
  `my_character_id` INT(11) NOT NULL,
  `character_id`    INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_ignore_my_character_id` (`my_character_id`),
  KEY `fk_dance_ignore_character_id` (`character_id`),
  CONSTRAINT `fk_dance_ignore_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_ignore_my_character_id` FOREIGN KEY (`my_character_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_item` (
  `id`             INT(11)      NOT NULL,
  `name`           VARCHAR(255) NOT NULL,
  `model_id`       INT(11)      NOT NULL,
  `category`       INT(11)      NOT NULL,
  `min_level`      INT(11)      NOT NULL,
  `duration`       INT(11)      NOT NULL,
  `price`          INT(11)      NOT NULL,
  `quantity`       INT(11)      NOT NULL,
  `sex`            INT(11)      NOT NULL,
  `price_category` INT(11)      NOT NULL,
  `wedding_ring`   INT(11)      NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_inventory` (
  `id`           INT(11) NOT NULL AUTO_INCREMENT,
  `character_id` INT(11) NOT NULL,
  `item_id`      INT(11) NOT NULL,
  `slot_number`  INT(11) NOT NULL,
  `quantity`     INT(11) NOT NULL,
  `expire_date`  INT(11) NOT NULL,
  `is_equipped`  INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_inventory_character_id` (`character_id`),
  KEY `fk_dance_inventory_item_id` (`item_id`),
  CONSTRAINT `fk_dance_inventory_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_inventory_item_id` FOREIGN KEY (`item_id`) REFERENCES `dance_item` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_mail` (
  `id`             INT(11)      NOT NULL AUTO_INCREMENT,
  `sender_id`      INT(11)      NOT NULL,
  `receiver_id`    INT(11)      NOT NULL,
  `gift_item_id`   INT(11)      NOT NULL,
  `date`           INT(11)      NOT NULL,
  `body`           TEXT         NOT NULL,
  `subject`        VARCHAR(255) NOT NULL,
  `type`           TINYINT(1)   NOT NULL,
  `read`           BIT(1)       NOT NULL,
  `special_sender` TINYINT(1)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_mail_sender_id` (`sender_id`),
  KEY `fk_dance_mail_receiver_id` (`receiver_id`),
  CONSTRAINT `fk_dance_mail_receiver_id` FOREIGN KEY (`receiver_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_mail_sender_id` FOREIGN KEY (`sender_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_setting` (
  `character_id`            INT(11) NOT NULL,
  `arrow_left`              INT(11) NOT NULL,
  `arrow_up`                INT(11) NOT NULL,
  `arrow_down`              INT(11) NOT NULL,
  `arrow_right`             INT(11) NOT NULL,
  `volume_background`       INT(11) NOT NULL,
  `volume_sound_effect`     INT(11) NOT NULL,
  `volume_game_music`       INT(11) NOT NULL,
  `note_panel_transparency` INT(11) NOT NULL,
  `video_soften`            INT(11) NOT NULL,
  `effects_scene`           INT(11) NOT NULL,
  `effects_avatar`          INT(11) NOT NULL,
  `camera_view`             INT(11) NOT NULL,
  `rate`                    INT(11) NOT NULL,
  KEY `fk_dance_setting_character_id` (`character_id`),
  CONSTRAINT `fk_dance_setting_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_song` (
  `id`            INT(11)      NOT NULL,
  `file_id`       INT(11)      NOT NULL,
  `name`          VARCHAR(255) NOT NULL,
  `key`           TEXT         NOT NULL,
  `notes_k_crc32` INT(11)      NOT NULL,
  `notes_k_key`   INT(11)      NOT NULL,
  `notes_k_size`  INT(11)      NOT NULL,
  `notes_t_crc32` INT(11)      NOT NULL,
  `notes_t_key`   INT(11)      NOT NULL,
  `notes_t_size`  INT(11)      NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_favorite_song` (
  `id`           INT(11) NOT NULL AUTO_INCREMENT,
  `song_id`      INT(11) NOT NULL,
  `character_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_favorite_song_song_id_character_id` (`song_id`, `character_id`),
  KEY `fk_dance_favorite_song_song_id` (`song_id`),
  KEY `fk_dance_favorite_song_character_id` (`character_id`),
  CONSTRAINT `fk_dance_favorite_song_song_id` FOREIGN KEY (`song_id`) REFERENCES `dance_song` (`id`),
  CONSTRAINT `fk_dance_favorite_song_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `dance_wedding` (
  `id`           INT(11)    NOT NULL AUTO_INCREMENT,
  `groom_id`     INT(11)    NOT NULL,
  `bride_id`     INT(11)    NOT NULL,
  `married_date` INT(11)    NOT NULL,
  `divorce_date` INT(11)    NOT NULL,
  `engage_date`  INT(11)    NOT NULL,
  `groom_state`  TINYINT(1) NOT NULL,
  `bride_state`  TINYINT(1) NOT NULL,
  `ring`         TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_wedding_groom_id` (`groom_id`),
  KEY `fk_dance_wedding_bride_id` (`bride_id`),
  CONSTRAINT `fk_dance_wedding_bride_id` FOREIGN KEY (`bride_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_wedding_groom_id` FOREIGN KEY (`groom_id`) REFERENCES `dance_character` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
