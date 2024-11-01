DROP TABLE IF EXISTS `spawn_data`;
CREATE TABLE `spawn_data` (
  `name` VARCHAR(80) NOT NULL,
  `status` SMALLINT NOT NULL,
  `current_hp` INT unsigned NOT NULL,
  `current_mp` INT unsigned NOT NULL,
  `loc_x` INT NOT NULL DEFAULT 0,
  `loc_y` INT NOT NULL DEFAULT 0,
  `loc_z` INT NOT NULL DEFAULT 0,
  `heading` MEDIUMINT NOT NULL DEFAULT 0,
  `db_value` SMALLINT NOT NULL DEFAULT 0,
  `respawn_time` BIGINT unsigned NOT NULL default 0,
  PRIMARY KEY (`name`)
);