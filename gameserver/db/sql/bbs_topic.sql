CREATE TABLE IF NOT EXISTS `bbs_topic` (
  `id` int(8) NOT NULL DEFAULT '0',
  `forum_id` int(8) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT '',
  `date` decimal(20,0) NOT NULL DEFAULT '0',
  `owner_name` varchar(255) NOT NULL DEFAULT '0',
  `owner_id` int(8) NOT NULL DEFAULT '0'
);