--liquibase formatted sql
--changeset ReachFive:create-welcome-message

CREATE TABLE welcome_message (
    message VARCHAR(25)
);

INSERT INTO welcome_message VALUES ('Welcome to Play');
