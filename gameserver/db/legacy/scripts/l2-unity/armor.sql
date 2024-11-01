 DROP TABLE IF EXISTS `ARMOR`;
CREATE TABLE `ARMOR` (
  `item_id` int(11) NOT NULL default '0',
  `name` varchar(70) default NULL,
  `bodypart` varchar(15) NOT NULL default '',
  `crystallizable` varchar(5) NOT NULL default '',
  `armor_type` varchar(5) NOT NULL default '',
  `weight` int(5) NOT NULL default '0',
  `material` varchar(15) NOT NULL default '',
  `crystal_type` varchar(4) NOT NULL default '',
  `avoid_modify` int(1) NOT NULL default '0',
  `duration` int(3) NOT NULL default '0',
  `p_def` int(3) NOT NULL default '0',
  `m_def` int(2) NOT NULL default '0',
  `mp_bonus` int(3) NOT NULL default '0',
  `price` int(11) NOT NULL default '0',
  `crystal_count` int(4) default NULL,
  `sellable` varchar(5) default NULL,
  `dropable` varchar(5) default NULL,
  `destroyable` varchar(5) default NULL,
  `tradeable` varchar(5) default NULL,
  PRIMARY KEY  (`item_id`)
);

-- 
-- Dumping data for table `armor`

INSERT INTO `ARMOR` VALUES 
 ('425','Apprentice''s Tunic','chest','false','magic','2150','cloth','none','0','-1','17','0','19','26','0','false','false','true','false'),
 ('461','Apprentice''s Stockings','legs','false','magic','1100','cloth','none','0','-1','10','0','10','6','0','false','false','true','false'),
 ('1146','Squire''s Shirt','chest','false','light','3301','cloth','none','0','-1','33','0','0','26','0','false','false','true','false'),
 ('1147','Squire''s Pants','legs','false','light','1750','cloth','none','0','-1','20','0','0','6','0','false','false','true','false');