DROP DATABASE IF EXISTS ttschool;
CREATE DATABASE `ttschool`; 
USE `ttschool`;

CREATE TABLE subject(
id INT(11) NOT NULL AUTO_INCREMENT,
subjectname VARCHAR(50) NOT NULL,
UNIQUE KEY subjectname (subjectname),
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE school(
id INT(11) NOT NULL AUTO_INCREMENT,
schoolname VARCHAR(50) NOT NULL,
year INT(4) NOT NULL, 
KEY schoolname (schoolname),
UNIQUE KEY school (schoolname, year),
PRIMARY KEY(id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE groups(
id INT(11) NOT NULL AUTO_INCREMENT,
schoolid INT(11) NOT NULL,
groupname VARCHAR(50) NOT NULL,
roomname VARCHAR(50) NOT NULL,
KEY groupname (groupname),
KEY roomname (roomname),
FOREIGN KEY (schoolid) REFERENCES school(id) ON DELETE CASCADE,
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE trainee(
id INT(11) NOT NULL AUTO_INCREMENT,
firstname VARCHAR(50) NOT NULL,
lastname VARCHAR(50) NOT NULL,
rating INT(11) NOT NULL,
groupsid INT(11),
PRIMARY KEY  (id),
FOREIGN KEY (groupsid) REFERENCES groups (id) ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE subject_groups(
id INT(11) NOT NULL auto_increment,
subjectid INT(11) NOT NULL,
groupsid INT(11) NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY subject_groups(subjectid, groupsid),
FOREIGN KEY (subjectid) REFERENCES subject (id) ON DELETE CASCADE,
FOREIGN KEY (groupsid) REFERENCES groups (id) ON DELETE CASCADE
)ENGINE=INNODB DEFAULT CHARSET=utf8;




