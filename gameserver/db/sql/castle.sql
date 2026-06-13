CREATE TABLE IF NOT EXISTS `castle` (
  `id` INT NOT NULL DEFAULT '0',
  `currentTaxPercent` INT NOT NULL DEFAULT '0',
  `nextTaxPercent` INT NOT NULL DEFAULT '0',
  `treasury` BIGINT NOT NULL DEFAULT '0',
  `taxRevenue` BIGINT NOT NULL DEFAULT '0',
  `seedIncome` BIGINT NOT NULL DEFAULT '0',
  `siegeDate` DECIMAL(20,0) NOT NULL DEFAULT '0',
  `regTimeOver` ENUM('true','false') DEFAULT 'true' NOT NULL,
  `certificates` SMALLINT NOT NULL DEFAULT '300',
  PRIMARY KEY (`id`)
);

INSERT IGNORE INTO `castle` VALUES
(1,15,15,0,0,0,0,'true',300),
(2,15,15,0,0,0,0,'true',300),
(3,15,15,0,0,0,0,'true',300),
(4,15,15,0,0,0,0,'true',300),
(5,15,15,0,0,0,0,'true',300),
(6,15,15,0,0,0,0,'true',300),
(7,15,15,0,0,0,0,'true',300),
(8,15,15,0,0,0,0,'true',300),
(9,15,15,0,0,0,0,'true',300);