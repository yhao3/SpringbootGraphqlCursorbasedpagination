CREATE TABLE IF NOT EXISTS   author(
    id         SERIAL        not null,
    first_name VARCHAR(100)  not null,
    last_name  VARCHAR(100)  not null,
    email      VARCHAR(100)  not null,
    primary key(id) 
); 
 
CREATE TABLE IF NOT EXISTS   post(
    id         SERIAL        not null,
    title      varchar(100)  not null,
    author_id  BIGINT        not null,
    created_at TIMESTAMP     not null,
    primary key(id)
);