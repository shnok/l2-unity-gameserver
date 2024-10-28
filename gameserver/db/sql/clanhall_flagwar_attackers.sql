CREATE TABLE IF NOT EXISTS `clanhall_flagwar_attackers` (
  `clanhall_id` TINYINT(2) UNSIGNED NOT NULL DEFAULT '0',
  `flag` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `npc` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `clan_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`flag`),
  KEY `hall_id` (`clanhall_id`),
  KEY `clan_id` (`clan_id`)
);