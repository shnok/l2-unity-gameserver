CREATE TABLE IF NOT EXISTS `seven_signs` (
  `char_obj_id` INT NOT NULL DEFAULT '0',
  `cabal` VARCHAR(8) NOT NULL DEFAULT 'NORMAL',
  `seal` VARCHAR(8) NOT NULL DEFAULT 'NONE',
  `red_stones` INT NOT NULL DEFAULT '0',
  `green_stones` INT NOT NULL DEFAULT '0',
  `blue_stones` INT NOT NULL DEFAULT '0',
  `ancient_adena_amount` DECIMAL(20,0) NOT NULL DEFAULT '0',
  `contribution_score` DECIMAL(20,0) NOT NULL DEFAULT '0',
  PRIMARY KEY  (`char_obj_id`)
);