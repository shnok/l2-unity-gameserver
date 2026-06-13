CREATE TABLE IF NOT EXISTS `clanhall_flagwar_members` (
  `clanhall_id` TINYINT(2) UNSIGNED NOT NULL DEFAULT '0',
  `clan_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `object_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  KEY `clanhall_id` (`clanhall_id`),
  KEY `clan_id` (`clan_id`),
  KEY `object_id` (`object_id`)
);