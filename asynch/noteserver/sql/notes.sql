DROP DATABASE IF EXISTS notes;
CREATE DATABASE notes; 
USE notes;

CREATE TABLE author (
    id INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login VARCHAR(50) NOT NULL,
    `password` VARCHAR(50) NOT NULL,
    rating double NOT NULL,
    createdNotes INT NOT NULL,
    timeRegistered DATETIME NOT NULL,
    `role` ENUM ('SUPER', 'REGULAR'),
    isDeleted BOOLEAN,
    UNIQUE KEY login (login),
    PRIMARY KEY (id)
);

CREATE TABLE `session` (
	id INT NOT NULL auto_increment,
    authorId INT NOT NULL,
    uuid VARCHAR(50),
    created DATETIME NOT NULL,
    FOREIGN KEY (authorId)
        REFERENCES author (id)
        ON DELETE CASCADE,
    UNIQUE KEY authorId (authorId),
    PRIMARY KEY(id)
);

CREATE TABLE section (
    id INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    authorId INT NOT NULL,
    FOREIGN KEY (authorId)
        REFERENCES author (id)
        ON DELETE CASCADE,
	UNIQUE KEY `name` (`name`),
    PRIMARY KEY (id)
);

CREATE TABLE note (
    id INT NOT NULL AUTO_INCREMENT,
    `subject` VARCHAR(50) NOT NULL,
    body MEDIUMTEXT NOT NULL,
    averageRating DOUBLE,
    rating INT,
    rates INT,
    sectionId INT NOT NULL,
    authorId INT NOT NULL,
    created DATETIME NOT NULL,
    FOREIGN KEY (authorId)
        REFERENCES author (id)
        ON DELETE CASCADE,
    FOREIGN KEY (sectionId)
        REFERENCES section (id)
        ON DELETE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE revision (
    id INT NOT NULL AUTO_INCREMENT,
    noteId INT,
    body MEDIUMTEXT NOT NULL,
    created DATETIME NOT NULL,
    FOREIGN KEY (noteId)
        REFERENCES note (id)
        ON DELETE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE `comment` (
    id INT NOT NULL AUTO_INCREMENT,
    body MEDIUMTEXT NOT NULL,
    authorId INT,
    noteId INT,
    revisionId INT,
    created datetime not null,
    FOREIGN KEY (authorId)
        REFERENCES author (id),
    FOREIGN KEY (revisionId)
        REFERENCES revision (id)
        ON DELETE CASCADE,
    PRIMARY KEY (id)
);

CREATE TABLE followed (
    id INT NOT NULL AUTO_INCREMENT,
    authorId INT NOT NULL,
    followedId INT NOT NULL,
    FOREIGN KEY (authorId)
        REFERENCES author (id)
        ON DELETE CASCADE,
    FOREIGN KEY (followedId)
        REFERENCES author (id)
        ON DELETE CASCADE,
    PRIMARY KEY (id),
    UNIQUE KEY followed (authorId, followedId)
);

CREATE TABLE ignored (
    id INT NOT NULL AUTO_INCREMENT,
    authorId INT NOT NULL,
    ignoredId INT NOT NULL,
    FOREIGN KEY (authorid)
        REFERENCES author (id)
        ON DELETE CASCADE,
    FOREIGN KEY (ignoredid)
        REFERENCES author (id)
        ON DELETE CASCADE,
    PRIMARY KEY (id),
    UNIQUE KEY followed (authorid, ignoredid)
);

CREATE TABLE rating (
    id INT NOT NULL AUTO_INCREMENT,
    noteId INT,
	authorId INT,
	`value` INT,
    FOREIGN KEY (noteId)
        REFERENCES note (id)
        ON DELETE CASCADE,
    FOREIGN KEY (authorId)
        REFERENCES author (id)
        ON DELETE CASCADE,
    UNIQUE KEY rating (noteId , authorId),
    PRIMARY KEY (id)
);



-- Added admin
INSERT into author VALUES (null, "admin", "admin", null, "admin", "admin", 0, 0, NOW(), 'SUPER', false);

