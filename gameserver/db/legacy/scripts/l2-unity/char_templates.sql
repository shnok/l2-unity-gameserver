-- 
-- Table structure for table `L2J_CHAR_TEMPLATES`
-- 

DROP TABLE IF EXISTS `CHAR_TEMPLATE`;
CREATE TABLE `CHAR_TEMPLATE` (
  `ClassId` int(11) NOT NULL default '0',
  `ClassName` varchar(20) NOT NULL default '',
  `RaceId` int(1) NOT NULL default '0',
  `STR` int(2) NOT NULL default '0',
  `CON` int(2) NOT NULL default '0',
  `DEX` int(2) NOT NULL default '0',
  `_INT` int(2) NOT NULL default '0',
  `WIT` int(2) NOT NULL default '0',
  `MEN` int(2) NOT NULL default '0',
  `P_ATK` int(3) NOT NULL default '0',
  `P_DEF` int(3) NOT NULL default '0',
  `M_ATK` int(3) NOT NULL default '0',
  `M_DEF` int(2) NOT NULL default '0',
  `P_SPD` int(3) NOT NULL default '0',
  `M_SPD` int(3) NOT NULL default '0',
  `ACC` int(3) NOT NULL default '0',
  `CRITICAL` int(3) NOT NULL default '0',
  `EVASION` int(3) NOT NULL default '0',
  `MOVE_SPD` int(3) NOT NULL default '0',
  `x` decimal(10,2) NOT NULL default '0',
  `y` decimal(10,2) NOT NULL default '0',
  `z` decimal(10,2) NOT NULL default '0',
  `items1` int(4) NOT NULL default '0',
  `items2` int(4) NOT NULL default '0',
  `items3` int(4) NOT NULL default '0',
  `items4` int(4) NOT NULL default '0',
  `items5` int(10) NOT NULL default '0',
  `F_COL_H` decimal(10,2) NOT NULL default '0',
  `F_COL_R` decimal(10,2) NOT NULL default '0',
  `M_COL_H` decimal(10,2) NOT NULL default '0',
  `M_COL_R` decimal(10,2) NOT NULL default '0',
  PRIMARY KEY  (`ClassId`)
);

INSERT INTO CHAR_TEMPLATES 
(ClassId, ClassName, RaceId, STR, CON, DEX, _INT, WIT, MEN, P_ATK, P_DEF, M_ATK, M_DEF, P_SPD, M_SPD, ACC, CRITICAL, EVASION, MOVE_SPD, x, y, z, items1, items2, items3, items4, items5, F_COL_H, F_COL_R, M_COL_H, M_COL_R)
SELECT ClassId, ClassName, RaceId, STR, CON, DEX, _INT, WIT, MEN, P_ATK, P_DEF, M_ATK, M_DEF, P_SPD, M_SPD, ACC, CRITICAL, EVASION, MOVE_SPD, (y / (52.5)), (z / (52.5)), (x / (52.5)), items1, items2, items3, items4, items5, (F_COL_H/52.5), (F_COL_R/52.5), (M_COL_H/52.5), (M_COL_R/52.5) FROM L2J_CHAR_TEMPLATES;



