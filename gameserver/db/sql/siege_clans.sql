CREATE TABLE IF NOT EXISTS `siege_clans` (
   `castle_id` TINYINT NOT NULL DEFAULT '0',
   `clan_id` INT(11) NOT NULL DEFAULT '0',
   `type` VARCHAR(8) DEFAULT 'PENDING',
   PRIMARY KEY (`castle_id`, `clan_id`)
);