CREATE TABLE IF NOT EXISTS `heroes` (
  `char_id` decimal(11,0) NOT NULL default '0',
  `class_id` decimal(3,0) NOT NULL default '0',
  `count` decimal(3,0) NOT NULL default '0',
  `played` decimal(1,0) NOT NULL default '0',
  `active` tinyint NOT NULL default 0,
  `message` varchar(300) NOT NULL default '',
  PRIMARY KEY  (`char_id`)
);