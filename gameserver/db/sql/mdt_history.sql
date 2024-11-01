CREATE TABLE IF NOT EXISTS `mdt_history` (
  `race_id` MEDIUMINT DEFAULT 0,
  `first` INT(1) DEFAULT 0,
  `second` INT(1) DEFAULT 0,
  `odd_rate` DOUBLE(10,2) DEFAULT 0,
  PRIMARY KEY (`race_id`)
);