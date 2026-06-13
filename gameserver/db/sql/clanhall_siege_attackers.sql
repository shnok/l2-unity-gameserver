CREATE TABLE IF NOT EXISTS `clanhall_siege_attackers` (
  `clanhall_id` TINYINT(2) NOT NULL DEFAULT '0',
  `attacker_id` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`clanhall_id`, `attacker_id`)
);