/*
Navicat MySQL Data Transfer

Source Server         : GA DB
Source Server Version : 50544
Source Host           : 172.18.216.82:3306
Source Database       : livestreaming

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2015-12-05 17:04:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for GameType
-- ----------------------------
DROP TABLE IF EXISTS `GameType`;
CREATE TABLE `GameType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `posterUrl` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of GameType
-- ----------------------------
INSERT INTO `GameType` VALUES ('1', '主机游戏', 'http://172.18.219.201/posters/default.jpg');

-- ----------------------------
-- Table structure for LiveRoom
-- ----------------------------
DROP TABLE IF EXISTS `LiveRoom`;
CREATE TABLE `LiveRoom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) NOT NULL,
  `roomName` varchar(50) NOT NULL,
  `roomDescription` text NOT NULL,
  `watchCount` int(11) unsigned zerofill DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `posterUrl` varchar(256) DEFAULT NULL,
  `gameType` int(11) NOT NULL,
  `pushUrl` varchar(256) NOT NULL,
  `serverIp` varchar(64) NOT NULL,
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`),
  KEY `id` (`userId`),
  KEY `gameType` (`gameType`),
  CONSTRAINT `id` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `LiveRoom_ibfk_1` FOREIGN KEY (`gameType`) REFERENCES `GameType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of LiveRoom
-- ----------------------------

-- ----------------------------
-- Table structure for Notice
-- ----------------------------
DROP TABLE IF EXISTS `Notice`;
CREATE TABLE `Notice` (
  `id` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `content` text NOT NULL,
  `posterUrl` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of Notice
-- ----------------------------

-- ----------------------------
-- Table structure for RecordRoom
-- ----------------------------
DROP TABLE IF EXISTS `RecordRoom`;
CREATE TABLE `RecordRoom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) NOT NULL,
  `roomName` varchar(50) NOT NULL,
  `roomDescription` text NOT NULL,
  `watchCount` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `posterUrl` varchar(256) NOT NULL,
  `gameType` int(11) NOT NULL,
  `playUrl` varchar(256) NOT NULL,
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `gameType` (`gameType`),
  CONSTRAINT `RecordRoom_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `RecordRoom_ibfk_2` FOREIGN KEY (`gameType`) REFERENCES `GameType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of RecordRoom
-- ----------------------------
INSERT INTO `RecordRoom` VALUES ('10', '99106813fd2c23e1a9f59352f2bff587ca07658b66d1cfd2a7d18fc3d552eabc', '撒野', '这是第一间直播房间', '0', '12', 'http://172.18.219.201/posters/99106813fd2c23e1a9f59352f2bff587ca07658b66d1cfd2a7d18fc3d552eabc.gif', '1', 'rtmp://172.18.219.201/videos/', '2015-12-05 16:59:30');

-- ----------------------------
-- Table structure for RtmpServer
-- ----------------------------
DROP TABLE IF EXISTS `RtmpServer`;
CREATE TABLE `RtmpServer` (
  `ip` varchar(20) NOT NULL,
  `app` varchar(20) DEFAULT NULL,
  `isAlive` bit(1) DEFAULT NULL,
  `clientCount` int(11) DEFAULT NULL,
  `pushUrl` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of RtmpServer
-- ----------------------------
INSERT INTO `RtmpServer` VALUES ('172.18.219.201', 'hls', null, null, 'rtmp://172.18.219.201/hls');

-- ----------------------------
-- Table structure for User
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `photoUrl` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of User
-- ----------------------------
INSERT INTO `User` VALUES ('12', '游戏达人', '123456', '455532734@qq.com', null, 'http://172.18.219.201/photos/default.jpg');
DROP TRIGGER IF EXISTS `createGameTypePosterUrl`;
DELIMITER ;;
CREATE TRIGGER `createGameTypePosterUrl` BEFORE INSERT ON `GameType` FOR EACH ROW IF LENGTH(new.posterUrl)<1 THEN
SET new.posterUrl = 'http://172.18.219.201/posters/default.jpg';
END IF
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `createLiveRoomPosterUrl`;
DELIMITER ;;
CREATE TRIGGER `createLiveRoomPosterUrl` BEFORE INSERT ON `LiveRoom` FOR EACH ROW IF LENGTH(new.posterUrl)<1 THEN
SET new.posterUrl=CONCAT('http://', new.serverIp, '/posters/', new.key , '.gif');
END IF
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `createURI`;
DELIMITER ;;
CREATE TRIGGER `createURI` BEFORE INSERT ON `RtmpServer` FOR EACH ROW SET new.pushUrl = CONCAT("rtmp://", new.ip, "/", new.app)
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `createPhotoUrl`;
DELIMITER ;;
CREATE TRIGGER `createPhotoUrl` BEFORE INSERT ON `User` FOR EACH ROW SET new.photoUrl = 'http://172.18.219.201/photos/default.jpg'
;;
DELIMITER ;
