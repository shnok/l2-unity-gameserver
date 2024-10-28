CREATE TABLE IF NOT EXISTS `clanhall_flagwar_owner_npcs` (
  `clanhall_id` TINYINT(2) UNSIGNED NOT NULL DEFAULT '0',
  `npc_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `clan_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`clanhall_id`),
  KEY `npc_id` (`npc_id`),
  KEY `clan_id` (`clan_id`)
);