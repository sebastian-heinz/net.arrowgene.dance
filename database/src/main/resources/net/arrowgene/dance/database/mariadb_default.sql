INSERT INTO `dance_channel` (`id`, `typ`, `position`, `name`) VALUES
  (1, 0, 0, 'Berlin'),
  (2, 0, 1, 'Rostock'),
  (3, 0, 2, 'Frankfurt'),
  (4, 1, 0, 'Paris'),
  (5, 1, 1, 'Lyon'),
  (6, 1, 2, 'Marseille'),
  (7, 5, 0, 'Deutschland-Tour'),
  (8, 5, 1, 'Frankreich-Tour');

INSERT INTO `account` (`id`, `name`, `hash`, `mail`, `mail_verified`, `mail_verified_at`, `mail_token`, `password_token`, `state`, `last_login`, `created`) VALUES
	(1, 'dance', '$2y$11$X/Cc7OBdLl4M6llMZwZCbewkwQqn49U5TOnqedzwE2Svox4/1ii8K', 'dance@arrowgene.net', 0, NULL, 'tUHHIv3nAI6ALA==', NULL, 100, NULL, '2018-07-15 03:58:49');

INSERT INTO dance_character (id, `name`, `level`, sex, flag, hair, glasses, top, shoes, face, gloves, pants, experience, games, wins, draws, losses, hearts, mvp, perfects, cools, bads, misses, points, coins, bonus, weight, ranking, status_achieved, best_score, age, zodiac, city, calorins_lost_week, points_won, competition_win, competition_lost, medal, alltime_best_ranking, tutorial, info, item_slot_count, cloth_slot_count) VALUES
    (1, 'dance', 1, 1, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, '', '', 0, 0, 0, 0, 0, 0, 0, '', 8, 8);
