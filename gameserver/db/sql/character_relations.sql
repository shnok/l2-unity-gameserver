CREATE TABLE IF NOT EXISTS `character_relations` (
  `char_id` INT UNSIGNED NOT NULL default 0,
  `friend_id` INT UNSIGNED NOT NULL DEFAULT 0,
  `relation` INT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`char_id`,`friend_id`)
);
