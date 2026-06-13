CREATE TABLE IF NOT EXISTS `character_recipebook` (
  `charId` INT UNSIGNED NOT NULL DEFAULT 0,
  `recipeId` SMALLINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`charId`,`recipeId`)
);