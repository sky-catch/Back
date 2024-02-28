DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS RESTAURANT;
DROP TABLE IF EXISTS RESERVATION;

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


CREATE TABLE RESTAURANT
(
    restaurant_id   bigint      NOT NULL AUTO_INCREMENT,
    owner_id        bigint      NOT NULL,
    name            varchar(25) NOT NULL,
    category        varchar(25) NOT NULL,
    content         text,
    phone           varchar(25) NOT NULL,
    capacity        int         NOT NULL,
    open_time       time        NOT NULL,
    last_order_time time        NOT NULL,
    address         varchar(50) NOT NULL,
    detail_address  varchar(25) NOT NULL,
    lunch_price     int                  DEFAULT NULL,
    dinner_price    int                  DEFAULT NULL,
    saved_count     bigint               DEFAULT NULL,
    review_count    bigint               DEFAULT NULL,
    review_avg      float                DEFAULT NULL,
    created_date    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (restaurant_id)
);


CREATE TABLE RESERVATION
(
    reservation_id     bigint      NOT NULL AUTO_INCREMENT,
    restaurant_id      bigint      NOT NULL,
    member_id          bigint      NOT NULL,
    reservation_day_id bigint      NOT NULL,
    payment_id         bigint      NOT NULL,
    time               timestamp   NOT NULL,
    number_of_people   int         NOT NULL,
    memo               varchar(255)         DEFAULT NULL,
    status             varchar(25) NOT NULL,
    created_date       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_id)
);