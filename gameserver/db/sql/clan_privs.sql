DROP TABLE IF EXISTS `clan_privs`;
CREATE TABLE `clan_privs` (
  `clan_id` INT NOT NULL DEFAULT'0',
  `ranking` INT NOT NULL DEFAULT '0',
  `privs` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`clan_id`,`ranking`)
);