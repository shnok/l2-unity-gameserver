CREATE TABLE IF NOT EXISTS `olympiad_fights` (
  `charOneId` int(10) unsigned NOT NULL,
  `charTwoId` int(10) unsigned NOT NULL,
  `charOneClass` tinyint(3) unsigned NOT NULL default '0',
  `charTwoClass` tinyint(3) unsigned NOT NULL default '0',
  `winner` tinyint(1) unsigned NOT NULL default '0',
  `start` bigint(13) unsigned NOT NULL DEFAULT '0',
  `time` bigint(13) unsigned NOT NULL DEFAULT '0',
  `classed` tinyint(1) unsigned NOT NULL default '0',
  KEY `charOneId` (`charOneId`),
  KEY `charTwoId` (`charTwoId`)
);