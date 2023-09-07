DROP TABLE IF EXISTS PUBLIC.REQUESTS;
DROP TABLE IF EXISTS PUBLIC.STATUSES;
DROP TABLE IF EXISTS PUBLIC.EVENT_COMPILATION;
DROP TABLE IF EXISTS PUBLIC.COMPILATIONS;
DROP TABLE IF EXISTS PUBLIC.EVENTS;
DROP TABLE IF EXISTS PUBLIC.STATES;
DROP TABLE IF EXISTS PUBLIC.LOCATIONS;
DROP TABLE IF EXISTS PUBLIC.CATEGORIES;
DROP TABLE IF EXISTS PUBLIC.USERS;

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    ID    INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    NAME  CHARACTER VARYING                            NOT NULL,
    EMAIL CHARACTER VARYING UNIQUE                     NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.CATEGORIES
(
    ID   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    NAME CHARACTER VARYING UNIQUE                     NOT NULL
);

CREATE TABLE IF NOT EXISTS PUBLIC.LOCATIONS
(
    ID           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    LOCATION_LAT FLOAT                                        NOT NULL,
    LOCATION_LON FLOAT                                        NOT NULL

);

CREATE TABLE IF NOT EXISTS PUBLIC.STATES
(
    ID   INTEGER NOT NULL PRIMARY KEY,
    NAME CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS PUBLIC.EVENTS
(
    ID                 INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    ANNOTATION         CHARACTER VARYING                            NOT NULL,
    CATEGORY_ID        INTEGER REFERENCES PUBLIC.CATEGORIES (ID)    NOT NULL,
    CONFIRMED_REQUESTS INTEGER,
    CREATED_ON         TIMESTAMP WITHOUT TIME ZONE                  NOT NULL,
    DESCRIPTION        CHARACTER VARYING                            NOT NULL,
    EVENT_DATE         TIMESTAMP WITHOUT TIME ZONE                  NOT NULL,
    INITIATOR_ID       INTEGER REFERENCES PUBLIC.USERS (ID)         NOT NULL,
    LOCATION_ID        INTEGER REFERENCES PUBLIC.LOCATIONS (ID)     NOT NULL,
--     LOCATION_LAT       FLOAT                                        NOT NULL,
--     LOCATION_LON       FLOAT                                        NOT NULL,
    PAID               BOOLEAN,
    PARTICIPANT_LIMIT  INTEGER,
    PUBLISHED_ON       TIMESTAMP WITHOUT TIME ZONE,
    REQUEST_MODERATION BOOLEAN,
    STATE              INTEGER REFERENCES PUBLIC.STATES (ID)        NOT NULL,
    TITLE              CHARACTER VARYING                            NOT NULL,
    VIEWS              INTEGER
);

CREATE TABLE IF NOT EXISTS PUBLIC.COMPILATIONS
(
    ID     INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    PINNED BOOLEAN,
    TITLE  CHARACTER VARYING                            NOT NULL
);


CREATE TABLE IF NOT EXISTS PUBLIC.EVENT_COMPILATION
(
    ID             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    EVENT_ID       INTEGER REFERENCES PUBLIC.EVENTS (ID),
    COMPILATION_ID INTEGER REFERENCES PUBLIC.COMPILATIONS (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.STATUSES
(
    ID   INTEGER NOT NULL PRIMARY KEY,
    NAME CHARACTER VARYING
);

CREATE TABLE IF NOT EXISTS PUBLIC.REQUESTS
(
    ID           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    CREATED      TIMESTAMP WITHOUT TIME ZONE                  NOT NULL,
    EVENT_ID     INTEGER REFERENCES PUBLIC.EVENTS (ID)        NOT NULL,
    REQUESTER_ID INTEGER REFERENCES PUBLIC.USERS (ID)         NOT NULL,
    STATUS       INTEGER REFERENCES PUBLIC.STATUSES (ID)      NOT NULL
);