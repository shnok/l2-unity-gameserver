CREATE TABLE IF NOT EXISTS `olympiad_nobles` (
  `char_id` INT UNSIGNED NOT NULL default 0,
  `class_id` tinyint(3) unsigned NOT NULL default 0,
  `olympiad_points` int(10) NOT NULL default 0,
  `competitions_done` smallint(3) NOT NULL default 0,
  `competitions_won` smallint(3) NOT NULL default 0,
  `competitions_lost` smallint(3) NOT NULL default 0,
  `competitions_drawn` smallint(3) NOT NULL default 0,
  `rewarded` smallint(1) NOT NULL default 0,
  PRIMARY KEY (`char_id`)
);