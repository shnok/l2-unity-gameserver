CREATE TABLE IF NOT EXISTS `castle_trapupgrade` (
  `castleId` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `towerIndex` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `level` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`towerIndex`,`castleId`)
);