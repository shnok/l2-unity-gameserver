-- 
-- Table structure for table `etcitem`
-- 

DROP TABLE IF EXISTS `etcitem`;
CREATE TABLE `etcitem` (
  `item_id` decimal(11,0) NOT NULL DEFAULT '0',
  `name` varchar(100) DEFAULT NULL,
  `crystallizable` varchar(5) DEFAULT NULL,
  `item_type` varchar(12) DEFAULT NULL,
  `weight` decimal(4,0) DEFAULT NULL,
  `consume_type` varchar(9) DEFAULT NULL,
  `material` varchar(11) DEFAULT NULL,
  `crystal_type` varchar(4) DEFAULT NULL,
  `duration` decimal(3,0) DEFAULT NULL,
  `price` decimal(11,0) DEFAULT NULL,
  `crystal_count` int(4) DEFAULT NULL,
  `sellable` varchar(5) DEFAULT NULL,
  `dropable` varchar(5) DEFAULT NULL,
  `destroyable` varchar(5) DEFAULT NULL,
  `tradeable` varchar(5) DEFAULT NULL,
  `id_name` varchar(100) NOT NULL DEFAULT '',
  `drop_category` enum('0','1','2') NOT NULL DEFAULT '2',
  PRIMARY KEY (`item_id`)
);

-- ----------------------------
-- Records of etcitem
-- ----------------------------
INSERT INTO `etcitem` VALUES ('17', 'Wooden Arrow', 'false', 'arrow', '6', 'stackable', 'wood', 'none', '-1', '2', '0', 'true', 'true', 'true', 'true', 'wooden_arrow', '2');
INSERT INTO `etcitem` VALUES ('57', 'Adena', 'false', 'none', '0', 'asset', 'gold', 'none', '-1', '1', '0', 'true', 'true', 'true', 'true', 'adena', '2');
INSERT INTO `etcitem` VALUES ('65', 'Red Potion', 'false', 'potion', '80', 'stackable', 'liquid', 'none', '-1', '40', '0', 'true', 'true', 'true', 'true', 'red_potion', '2');
INSERT INTO `etcitem` VALUES ('1835', 'Soulshot: No Grade', 'false', 'shot', '4', 'stackable', 'paper', 'none', '-1', '7', '0', 'true', 'true', 'true', 'true', 'soulshot_none', '2');
INSERT INTO `etcitem` VALUES ('2509', 'Spiritshot: No Grade', 'false', 'shot', '5', 'stackable', 'paper', 'none', '-1', '15', '0', 'true', 'true', 'true', 'true', 'spiritshot_none', '2');
INSERT INTO `etcitem` VALUES ('3947', 'Blessed Spiritshot: No Grade', 'false', 'shot', '5', 'stackable', 'paper', 'none', '-1', '35', '0', 'true', 'true', 'true', 'true', 'blessed_spiritshot_none', '2');