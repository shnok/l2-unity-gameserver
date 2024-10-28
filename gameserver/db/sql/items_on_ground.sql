CREATE TABLE IF NOT EXISTS `items_on_ground` (
  `object_id` int(11) NOT NULL default '0',
  `item_id` int(11) default NULL,
  `count` int(11) default NULL,
  `enchant_level` int(11) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `z` int(11) default NULL,
  `time` decimal(20,0) default NULL,
  PRIMARY KEY  (`object_id`)
);