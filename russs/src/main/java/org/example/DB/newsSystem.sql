CREATE DATABASE IF NOT EXISTS newsSystem;
USE newsSystem;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `username` varchar(60) COLLATE utf8_bin NOT NULL,
                        `password` varchar(60) COLLATE utf8_bin NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1112 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'pyb', '123');
COMMIT;


SET FOREIGN_KEY_CHECKS = 1;
