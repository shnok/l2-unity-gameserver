CREATE TABLE IF NOT EXISTS clan_skills (
  clan_id INT NOT NULL DEFAULT 0,
  skill_id INT NOT NULL DEFAULT 0,
  skill_level INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`clan_id`,`skill_id`)
);