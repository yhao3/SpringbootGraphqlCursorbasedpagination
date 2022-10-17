CREATE TABLE IF NOT EXISTS author(
    id         BIGINT not null AUTO_INCREMENT,
    first_name VARCHAR(100) not null,
    last_name  VARCHAR(100) not null,
    email      VARCHAR(100) not null,
    primary key(id)
);

CREATE TABLE IF NOT EXISTS post(
    id        BIGINT not null AUTO_INCREMENT,
    title     varchar(100) not null,
    author_id BIGINT not null,
    primary key(id)
);