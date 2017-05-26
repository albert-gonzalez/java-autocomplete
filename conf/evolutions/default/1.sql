# Games schema

# --- !Ups

CREATE TABLE Game (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(512) NOT NULL,
    description longtext NOT NULL,
    url varchar(1024) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Game;