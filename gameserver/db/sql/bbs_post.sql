CREATE TABLE IF NOT EXISTS `bbs_post` (
  `id` int(8) NOT NULL DEFAULT '0',
  `owner_name` varchar(255) NOT NULL DEFAULT '',
  `owner_id` int(8) NOT NULL DEFAULT '0',
  `date` decimal(20,0) NOT NULL DEFAULT '0',
  `topic_id` int(8) NOT NULL DEFAULT '0',
  `forum_id` int(8) NOT NULL DEFAULT '0',
  `txt` text NOT NULL
);