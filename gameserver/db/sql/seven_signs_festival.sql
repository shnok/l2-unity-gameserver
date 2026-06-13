CREATE TABLE IF NOT EXISTS `seven_signs_festival` (
`festivalId` int(1) NOT NULL DEFAULT '0',
`cabal` varchar(4) NOT NULL DEFAULT '',
`cycle` int(4) NOT NULL DEFAULT '0',
`date` bigint(50) DEFAULT '0',
`score` int(5) NOT NULL DEFAULT '0',
`members` varchar(255) NOT NULL DEFAULT '',
PRIMARY KEY (`festivalId`,`cabal`,`cycle`)
);

INSERT IGNORE INTO `seven_signs_festival` VALUES 
(0, "DAWN", 1, 0, 0, ""),
(1, "DAWN", 1, 0, 0, ""),
(2, "DAWN", 1, 0, 0, ""),
(3, "DAWN", 1, 0, 0, ""),
(4, "DAWN", 1, 0, 0, ""),
(0, "DUSK", 1, 0, 0, ""),
(1, "DUSK", 1, 0, 0, ""),
(2, "DUSK", 1, 0, 0, ""),
(3, "DUSK", 1, 0, 0, ""),
(4, "DUSK", 1, 0, 0, "");