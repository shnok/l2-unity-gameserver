CREATE TABLE IF NOT EXISTS `augmentations` (
	`item_oid` INT UNSIGNED NOT NULL DEFAULT 0,
	`attributes` INT NOT NULL DEFAULT -1,
	`skill_id` INT NOT NULL DEFAULT -1,
	`skill_level` INT NOT NULL DEFAULT -1,
	PRIMARY KEY (`item_oid`)
);