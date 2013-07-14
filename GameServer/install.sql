CREATE DATABASE IF NOT EXISTS Game;

CREATE USER 'gameServer'@'localhost' IDENTIFIED BY 'gameServerPassword';

GRANT ALL PRIVILEGES ON Game.* TO 'gameServer'@'localhost' IDENTIFIED BY 'gameServerPassword' WITH GRANT OPTION;

USE Game;

CREATE TABLE IF NOT EXISTS `Account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `emailInvalid` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 |

CREATE TABLE IF NOT EXISTS `PlayerCharacter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE IF NOT EXISTS `LoginAttempt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` bigint(20) DEFAULT NULL,
  `successful` tinyint(1) DEFAULT NULL,
  `connection` text,
  `account` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8

CREATE TABLE IF NOT EXISTS `RegistrationAttempt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` bigint(20) DEFAULT NULL,
  `successful` tinyint(1) DEFAULT NULL,
  `connection` text,
  `account` int(11) DEFAULT '0',
  `email` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 |
