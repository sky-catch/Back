DROP TABLE IF EXISTS MEMBER;

CREATE TABLE MEMBER
(
    member_id         bigint      NOT NULL AUTO_INCREMENT,
    nickname          varchar(25) NOT NULL,
    profile_image_url varchar(255)         DEFAULT NULL,
    email             varchar(25) NOT NULL,
    name              varchar(25) NOT NULL,
    status            varchar(25) NOT NULL,
    oauth_server_id   varchar(25) NOT NULL,
    oauth_server      varchar(25) NOT NULL,
    created_date      timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date      timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id)
);