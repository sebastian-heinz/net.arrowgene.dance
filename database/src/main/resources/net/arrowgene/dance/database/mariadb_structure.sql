/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE IF NOT EXISTS `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(17) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `mail_verified` tinyint(1) NOT NULL,
  `mail_verified_at` datetime DEFAULT NULL,
  `mail_token` varchar(255) DEFAULT NULL,
  `password_token` varchar(255) DEFAULT NULL,
  `state` int(11) NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_account_name` (`name`),
  UNIQUE KEY `uq_account_mail` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typ` int(11) NOT NULL,
  `position` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_channel_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_character` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  `sex` int(11) NOT NULL,
  `flag` int(11) NOT NULL,
  `hair` int(11) NOT NULL,
  `glasses` int(11) NOT NULL,
  `top` int(11) NOT NULL,
  `shoes` int(11) NOT NULL,
  `face` int(11) NOT NULL,
  `gloves` int(11) NOT NULL,
  `pants` int(11) NOT NULL,
  `experience` int(11) NOT NULL,
  `games` int(11) NOT NULL,
  `wins` int(11) NOT NULL,
  `draws` int(11) NOT NULL,
  `losses` int(11) NOT NULL,
  `hearts` int(11) NOT NULL,
  `mvp` int(11) NOT NULL,
  `perfects` int(11) NOT NULL,
  `cools` int(11) NOT NULL,
  `bads` int(11) NOT NULL,
  `misses` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `coins` int(11) NOT NULL,
  `bonus` int(11) NOT NULL,
  `weight` int(11) NOT NULL,
  `ranking` int(11) NOT NULL,
  `status_achieved` int(11) NOT NULL,
  `best_score` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `zodiac` varchar(11) NOT NULL,
  `city` varchar(11) NOT NULL,
  `calorins_lost_week` int(11) NOT NULL,
  `points_won` int(11) NOT NULL,
  `competition_win` int(11) NOT NULL,
  `competition_lost` int(11) NOT NULL,
  `medal` int(11) NOT NULL,
  `alltime_best_ranking` int(11) NOT NULL,
  `tutorial` int(11) NOT NULL,
  `info` varchar(255) NOT NULL,
  `item_slot_count` int(11) NOT NULL,
  `cloth_slot_count` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_character_name` (`name`),
  KEY `fk_dance_character_account_id` (`account_id`),
  CONSTRAINT `fk_dance_character_account_id` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_favorite_song` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `song_id` int(11) NOT NULL,
  `character_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_favorite_song_song_id_character_id` (`song_id`,`character_id`),
  KEY `fk_dance_favorite_song_character_id` (`character_id`),
  CONSTRAINT `fk_dance_favorite_song_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_favorite_song_song_id` FOREIGN KEY (`song_id`) REFERENCES `dance_song` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `my_character_id` int(11) NOT NULL,
  `character_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_friend_my_character_id` (`my_character_id`),
  KEY `fk_dance_friend_character_id` (`character_id`),
  CONSTRAINT `fk_dance_friend_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_friend_my_character_id` FOREIGN KEY (`my_character_id`) REFERENCES `dance_character` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `date` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `slogan` varchar(255) NOT NULL,
  `introduction` varchar(255) NOT NULL,
  `icon` int(11) NOT NULL,
  `leader_id` int(11) NOT NULL,
  `notice_text` varchar(255) NOT NULL,
  `notice_title` varchar(255) NOT NULL,
  `notice_date` int(11) NOT NULL,
  `max_members` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_dance_group_name` (`name`),
  KEY `fk_dance_group_leader_id` (`leader_id`),
  CONSTRAINT `fk_dance_group_leader_id` FOREIGN KEY (`leader_id`) REFERENCES `dance_character` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_group_member` (
  `character_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `join_date` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `rights` int(11) NOT NULL,
  KEY `fk_dance_group_member_character_id` (`character_id`),
  KEY `fk_dance_group_member_group_id` (`group_id`),
  CONSTRAINT `fk_dance_group_member_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_group_member_group_id` FOREIGN KEY (`group_id`) REFERENCES `dance_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_ignore` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `my_character_id` int(11) NOT NULL,
  `character_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_ignore_my_character_id` (`my_character_id`),
  KEY `fk_dance_ignore_character_id` (`character_id`),
  CONSTRAINT `fk_dance_ignore_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_ignore_my_character_id` FOREIGN KEY (`my_character_id`) REFERENCES `dance_character` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `character_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `slot_number` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `expire_date` int(11) NOT NULL,
  `is_equipped` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_inventory_character_id` (`character_id`),
  KEY `fk_dance_inventory_item_id` (`item_id`),
  CONSTRAINT `fk_dance_inventory_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_inventory_item_id` FOREIGN KEY (`item_id`) REFERENCES `dance_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_item` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `model_id` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  `min_level` int(11) NOT NULL,
  `duration` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `sex` int(11) NOT NULL,
  `price_category` int(11) NOT NULL,
  `wedding_ring` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `gift_item_id` int(11) NOT NULL,
  `date` int(11) NOT NULL,
  `body` text NOT NULL,
  `subject` varchar(255) NOT NULL,
  `type` tinyint(1) NOT NULL,
  `read` bit(1) NOT NULL,
  `special_sender` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_mail_sender_id` (`sender_id`),
  KEY `fk_dance_mail_receiver_id` (`receiver_id`),
  CONSTRAINT `fk_dance_mail_receiver_id` FOREIGN KEY (`receiver_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_mail_sender_id` FOREIGN KEY (`sender_id`) REFERENCES `dance_character` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_setting` (
  `character_id` int(11) NOT NULL,
  `arrow_left` int(11) NOT NULL,
  `arrow_up` int(11) NOT NULL,
  `arrow_down` int(11) NOT NULL,
  `arrow_right` int(11) NOT NULL,
  `volume_background` int(11) NOT NULL,
  `volume_sound_effect` int(11) NOT NULL,
  `volume_game_music` int(11) NOT NULL,
  `note_panel_transparency` int(11) NOT NULL,
  `video_soften` int(11) NOT NULL,
  `effects_scene` int(11) NOT NULL,
  `effects_avatar` int(11) NOT NULL,
  `camera_view` int(11) NOT NULL,
  `rate` int(11) NOT NULL,
  KEY `fk_dance_setting_character_id` (`character_id`),
  CONSTRAINT `fk_dance_setting_character_id` FOREIGN KEY (`character_id`) REFERENCES `dance_character` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_song` (
  `id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `key` text NOT NULL,
  `notes_k_crc32` int(11) NOT NULL,
  `notes_k_key` int(11) NOT NULL,
  `notes_k_size` int(11) NOT NULL,
  `notes_t_crc32` int(11) NOT NULL,
  `notes_t_key` int(11) NOT NULL,
  `notes_t_size` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `dance_wedding` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groom_id` int(11) NOT NULL,
  `bride_id` int(11) NOT NULL,
  `married_date` int(11) NOT NULL,
  `divorce_date` int(11) NOT NULL,
  `engage_date` int(11) NOT NULL,
  `groom_state` tinyint(1) NOT NULL,
  `bride_state` tinyint(1) NOT NULL,
  `ring` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dance_wedding_groom_id` (`groom_id`),
  KEY `fk_dance_wedding_bride_id` (`bride_id`),
  CONSTRAINT `fk_dance_wedding_bride_id` FOREIGN KEY (`bride_id`) REFERENCES `dance_character` (`id`),
  CONSTRAINT `fk_dance_wedding_groom_id` FOREIGN KEY (`groom_id`) REFERENCES `dance_character` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
