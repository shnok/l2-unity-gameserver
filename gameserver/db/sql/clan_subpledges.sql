CREATE TABLE IF NOT EXISTS `clan_subpledges` (
  `clan_id` INT NOT NULL default '0',
  `sub_pledge_id` INT NOT NULL default '0',
  `name` varchar(45),
  `leader_id` INT NOT NULL default '0',
  PRIMARY KEY (`clan_id`,`sub_pledge_id`)
);