CREATE TABLE IF NOT EXISTS `seven_signs_status` (
  `id` int(3) NOT NULL default '0',
  `current_cycle` int(10) NOT NULL DEFAULT '1',
  `festival_cycle` int(10) NOT NULL DEFAULT '1',
  `active_period` VARCHAR(16) NOT NULL DEFAULT 'COMPETITION',
  `date` bigint(13) unsigned NOT NULL DEFAULT '0',
  `previous_winner` VARCHAR(8) NOT NULL DEFAULT 'NORMAL',
  `dawn_stone_score` DECIMAL(20,0) NOT NULL DEFAULT '0',
  `dawn_festival_score` int(10) NOT NULL DEFAULT '0',
  `dusk_stone_score` DECIMAL(20,0) NOT NULL DEFAULT '0',
  `dusk_festival_score` int(10) NOT NULL DEFAULT '0',
  `avarice_owner` VARCHAR(8) NOT NULL DEFAULT 'NORMAL',
  `gnosis_owner` VARCHAR(8) NOT NULL DEFAULT 'NORMAL',
  `strife_owner` VARCHAR(8) NOT NULL DEFAULT 'NORMAL',
  `avarice_dawn_score` int(10) NOT NULL DEFAULT '0',
  `gnosis_dawn_score` int(10) NOT NULL DEFAULT '0',
  `strife_dawn_score` int(10) NOT NULL DEFAULT '0',
  `avarice_dusk_score` int(10) NOT NULL DEFAULT '0',
  `gnosis_dusk_score` int(10) NOT NULL DEFAULT '0',
  `strife_dusk_score` int(10) NOT NULL DEFAULT '0',
  `accumulated_bonus0` int(10) NOT NULL DEFAULT '0',
  `accumulated_bonus1` int(10) NOT NULL DEFAULT '0',
  `accumulated_bonus2` int(10) NOT NULL DEFAULT '0',
  `accumulated_bonus3` int(10) NOT NULL DEFAULT '0',
  `accumulated_bonus4` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY  (`id`)
);

INSERT IGNORE INTO `seven_signs_status` VALUES
(0,1,1,'COMPETITION',0,'NORMAL',0,0,0,0,'NORMAL','NORMAL','NORMAL',0,0,0,0,0,0,0,0,0,0,0);