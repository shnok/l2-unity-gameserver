CREATE TABLE IF NOT EXISTS `character_raid_points` (
  `char_id` INT UNSIGNED NOT NULL DEFAULT 0,
  `boss_id` INT UNSIGNED NOT NULL DEFAULT 0,
  `points` INT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`char_id`,`boss_id`)
);