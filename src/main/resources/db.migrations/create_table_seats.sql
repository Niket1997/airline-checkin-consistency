CREATE TABLE IF NOT EXISTS seats
(
    id      int        NOT NULL PRIMARY KEY,
    name    varchar(4) NOT NULL,
    trip_id int        NOT NULL,
    user_id int
);