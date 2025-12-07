-- Database Initialization Script for RuralSync

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT 'User Name',
  `password` varchar(100) DEFAULT NULL COMMENT 'Password (Encrypted)',
  `role` varchar(20) NOT NULL COMMENT 'Role: VILLAGER, OFFICIAL, ADMIN',
  `phone` varchar(20) DEFAULT NULL COMMENT 'Phone Number',
  `openid` varchar(64) DEFAULT NULL COMMENT 'WeChat OpenID',
  `points` int(11) DEFAULT 0 COMMENT 'Points Balance',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User Information';

-- ----------------------------
-- Table structure for issues
-- ----------------------------
DROP TABLE IF EXISTS `issues`;
CREATE TABLE `issues` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reporter_id` bigint(20) NOT NULL COMMENT 'Reporter User ID',
  `type` varchar(50) NOT NULL COMMENT 'Type: FACILITY, HYGIENE, DISPUTE, OTHER',
  `content` text COMMENT 'Description',
  `media_urls` text COMMENT 'JSON array of media URLs (images/videos/audio)',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT 'Status: PENDING, PROCESSING, DONE',
  `current_handler_id` bigint(20) DEFAULT NULL COMMENT 'Current Handler User ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_reporter` (`reporter_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Issue Reports (Smart Reporting)';

-- ----------------------------
-- Table structure for issue_logs
-- ----------------------------
DROP TABLE IF EXISTS `issue_logs`;
CREATE TABLE `issue_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `issue_id` bigint(20) NOT NULL,
  `operator_id` bigint(20) DEFAULT NULL COMMENT 'Operator User ID',
  `action` varchar(50) NOT NULL COMMENT 'Action: SUBMIT, ASSIGN, COMPLETE, REJECT',
  `comment` varchar(255) DEFAULT NULL COMMENT 'Process Comment',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_issue` (`issue_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Issue Processing Logs';

-- ----------------------------
-- Table structure for notices
-- ----------------------------
DROP TABLE IF EXISTS `notices`;
CREATE TABLE `notices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `category` varchar(20) NOT NULL COMMENT 'Category: PARTY, VILLAGE, FINANCE',
  `content` longtext COMMENT 'HTML Content',
  `publish_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Public Notices';

-- ----------------------------
-- Table structure for market_items
-- ----------------------------
DROP TABLE IF EXISTS `market_items`;
CREATE TABLE `market_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `type` varchar(20) NOT NULL COMMENT 'Type: RENT, SELL, HELP',
  `title` varchar(100) NOT NULL,
  `description` text,
  `price` decimal(10,2) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, SOLD, REMOVED',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Marketplace Items';

-- ----------------------------
-- Table structure for community_posts
-- ----------------------------
DROP TABLE IF EXISTS `community_posts`;
CREATE TABLE `community_posts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `content` text,
  `media_urls` text COMMENT 'JSON array of images',
  `likes` int(11) DEFAULT 0,
  `comments_count` int(11) DEFAULT 0,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Community Posts';

-- ----------------------------
-- Table structure for points_records
-- ----------------------------
DROP TABLE IF EXISTS `points_records`;
CREATE TABLE `points_records` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `change_amount` int(11) NOT NULL COMMENT 'Positive or Negative',
  `reason` varchar(100) NOT NULL COMMENT 'Reason: REPORT_ISSUE, ACTIVITY_JOIN',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Points History';

-- ----------------------------
-- Table structure for officials
-- ----------------------------
DROP TABLE IF EXISTS `officials`;
CREATE TABLE `officials` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `position` varchar(50) NOT NULL COMMENT 'Position Title',
  `phone` varchar(20) NOT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  `display_order` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Officials Directory';

SET FOREIGN_KEY_CHECKS = 1;
