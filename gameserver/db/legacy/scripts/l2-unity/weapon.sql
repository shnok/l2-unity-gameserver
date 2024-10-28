-- 
-- Table structure for table `weapon`
-- 

DROP TABLE IF EXISTS `weapon`;
CREATE TABLE `weapon` (
  `item_id` decimal(11,0) NOT NULL default '0',
  `name` varchar(70) default NULL,
  `bodypart` varchar(15) default NULL,
  `crystallizable` varchar(5) default NULL,
  `weight` decimal(4,0) default NULL,
  `soulshots` decimal(2,0) default NULL,
  `spiritshots` decimal(1,0) default NULL,
  `material` varchar(11) default NULL,
  `crystal_type` varchar(4) default NULL,
  `p_dam` decimal(5,0) default NULL,
  `rnd_dam` decimal(2,0) default NULL,
  `weaponType` varchar(8) default NULL,
  `critical` decimal(2,0) default NULL,
  `hit_modify` decimal(6,5) default NULL,
  `avoid_modify` decimal(2,0) default NULL,
  `shield_def` decimal(3,0) default NULL,
  `shield_def_rate` decimal(2,0) default NULL,
  `atk_speed` decimal(3,0) default NULL,
  `mp_consume` decimal(2,0) default NULL,
  `m_dam` decimal(3,0) default NULL,
  `duration` decimal(3,0) default NULL,
  `price` decimal(11,0) default NULL,
  `crystal_count` int(4) default NULL,
  `sellable` varchar(5) default NULL,
  `dropable` varchar(5) default NULL,
  `destroyable` varchar(5) default NULL,
  `tradeable` varchar(5) default NULL,
  PRIMARY KEY  (`item_id`)
);

-- 
-- Dumping data for table `weapon`
-- 

INSERT INTO `weapon` VALUES 
 ('1','Short Sword','rhand','false','1600','1','1','steel','none','8','10','sword','8','0.00000','0','0','0','379','0','6','-1','768','0','true','true','true','true'),
 ('2','Long Sword','rhand','false','1560','2','2','fine_steel','none','24','10','sword','8','0.00000','0','0','0','379','0','17','-1','136000','0','true','true','true','true'),
 ('3','Broadsword','rhand','false','1590','1','1','steel','none','11','10','sword','8','0.00000','0','0','0','379','0','9','-1','12500','0','true','true','true','true'),
 ('4','Club','rhand','false','1870','1','1','wood','none','8','20','blunt','4','4.00000','0','0','0','379','0','6','-1','768','0','true','true','true','true'),
 ('5','Mace','rhand','false','1880','1','1','steel','none','11','20','blunt','4','4.00000','0','0','0','379','0','9','-1','12500','0','true','true','true','true'),
 ('6','Apprentice''s Wand','rhand','false','1350','1','1','steel','none','5','20','blunt','4','4.00000','0','0','0','379','0','7','-1','138','0','false','false','true','false'),
 ('7','Apprentice''s Rod','rhand','false','1330','1','1','wood','none','6','20','blunt','4','4.00000','0','0','0','379','0','8','-1','768','0','true','true','true','true'),
 ('10','Dagger','rhand','false','1160','1','1','steel','none','5','5','dagger','12','-3.00000','0','0','0','433','0','5','-1','138','0','false','false','true','false'),
 ('14','Bow','lrhand','false','1930','1','1','wood','none','23','5','bow','12','-3.00000','0','0','0','293','1','9','-1','12500','0','true','true','true','true'),
 ('20','Buckler','lhand','false','1410','0','0','wood','none','0','0','none','0','0.00000','-8','67','20','0','0','0','-1','2780','0','true','true','true','true'),
 ('102','Round Shield','lhand','false','1390','0','0','steel','none','0','0','none','0','0.00000','-8','79','20','0','0','0','-1','7110','0','true','true','true','true'),
 ('89','Big Hammer','rhand','true','1710','2','2','fine_steel','c','107','20','blunt','4','4.00000','0','0','0','379','0','61','-1','2290000','916','true','true','true','true'),
 ('129','Sword of Revolution','rhand','true','1450','3','3','fine_steel','d','79','10','sword','8','0.00000','0','0','0','379','0','47','-1','1400000','2545','true','true','true','true'),
 ('177','Mage Staff','lrhand','false','1050','2','2','wood','none','30','20','bigblunt','4','4.00000','0','0','0','325','0','28','-1','244000','0','true','true','true','true'),
 ('156','Hand Axe','rhand','true','1820','2','2','steel','d','40','20','blunt','4','4.00000','0','0','0','379','0','26','-1','409000','743','true','true','true','true'),
 ('275','Long Bow','lrhand','true','1830','6','2','steel','d','114','5','bow','12','-3.00000','0','0','0','227','4','35','-1','644000','1170','true','true','true','true'),
 ('2369','Squire''s Sword','rhand','false','1600','1','1','steel','none','6','10','sword','8','0.00000','0','0','0','379','0','5','-1','138','0','false','false','true','false'),
 ('2370','Guild Member''s Club','rhand','false','1910','1','1','wood','none','6','20','blunt','4','4.00000','0','0','0','379','0','5','-1','138','0','false','false','true','false'),
 ('5284','Zweihander','lrhand','false','1530','2','2','bronze','none','38','10','bigsword','8','0.00000','0','0','0','325','0','21','-1','244000','0','true','true','true','true');
 