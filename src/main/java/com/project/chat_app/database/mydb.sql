create database chat_application;

use chat_application;

CREATE TABLE users (userId VARCHAR(20) PRIMARY KEY NOT NULL UNIQUE,
                    name VARCHAR(30),
                    pass VARCHAR(30),
                    joiningTime date
                   );

CREATE TABLE chat_backup (
                    sid VARCHAR(20),
                    rid VARCHAR(20),
                    msg VARCHAR(200),
                    time date
                    );