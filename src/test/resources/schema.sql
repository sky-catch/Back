DROP TABLE if EXISTS OAUTH_MEMBER;

CREATE TABLE OAUTH_MEMBER
(
    id bigint auto_increment primary key,
    nickname varchar(20) not null,
    profile_image_url varchar(100) null,
    email varchar(20) null,
    name varchar(20) null,
    oauth_server_id varchar(20) not null,
    oauth_server varchar(20) not null
);