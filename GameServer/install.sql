CREATE DATABASE IF NOT EXISTS Game;

CREATE USER 'gameServer'@'localhost' IDENTIFIED BY 'gameServerPassword';

GRANT ALL PRIVILEGES ON *.* TO 'gameServer'@'localhost' IDENTIFIED BY 'gameServerPassword' WITH GRANT OPTION;

USE Game;

CREATE TABLE IF NOT EXISTS Account (
id INT(11) NOT NULL AUTO_INCREMENT,
username VARCHAR(50) NOT NULL,
password VARCHAR(255),
email VARCHAR(255),
PRIMARY KEY (Id));

CREATE TABLE IF NOT EXISTS GameCharacter (
id INT(11) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (Id));

CREATE TABLE IF NOT EXISTS LoginAttempt (
id INT(11) NOT NULL AUTO_INCREMENT,
time INT(11) DEFAULT 0,
successful BOOLEAN,
connection text,
account INT(11) DEFAULT 0,
PRIMARY KEY (Id));
