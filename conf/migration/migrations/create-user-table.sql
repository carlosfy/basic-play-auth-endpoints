CREATE TABLE users (
    id              SERIAL,
    username        VARCHAR(25)     NOT NULL, 
    password_hash   INTEGER         NOT NULL, 
    age             smallint, 
    gender          VARCHAR(25),

    PRIMARY KEY (id), 
    UNIQUE (username)
)