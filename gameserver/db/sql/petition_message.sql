CREATE TABLE IF NOT EXISTS `petition_message` (
  `id` INT UNSIGNED NOT NULL DEFAULT 0,
  `petition_oid` INT UNSIGNED NOT NULL DEFAULT 0,
  `player_oid` INT UNSIGNED NOT NULL DEFAULT 0,
  `type` VARCHAR(20) NOT NULL,
  `player_name` VARCHAR(20) NOT NULL,
  `content` VARCHAR(120) NOT NULL,
  PRIMARY KEY  (`id`, `petition_oid`)
);