CREATE TABLE IF NOT EXISTS `items` (
  `owner_id` INT,
  `object_id` INT NOT NULL DEFAULT 0,
  `item_id` SMALLINT UNSIGNED NOT NULL,
  `count` INT UNSIGNED NOT NULL DEFAULT 0,
  `enchant_level` SMALLINT UNSIGNED NOT NULL DEFAULT 0,
  `loc` VARCHAR(10),
  `loc_data` INT,
  `custom_type1` INT NOT NULL DEFAULT 0,
  `custom_type2` INT NOT NULL DEFAULT 0,
  `mana_left` INT NOT NULL DEFAULT -1,
  `time` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`object_id`)
);