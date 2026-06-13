DROP TABLE IF EXISTS `clan_wars`;
CREATE TABLE `clan_wars` (
  `clan1` varchar(35) NOT NULL DEFAULT '',
  `clan2` varchar(35) NOT NULL DEFAULT '',
  `expiry_time` decimal(20,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`clan1`,`clan2`)
);