CREATE TABLE alarm
(
    alarm_id       bigint      NOT NULL AUTO_INCREMENT,
    reservation_id bigint      NOT NULL,
    status         varchar(25) NOT NULL,
    created_date   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (alarm_id)
);


CREATE TABLE comment
(
    comment_id   bigint    NOT NULL AUTO_INCREMENT,
    review_id    bigint    NOT NULL,
    owner_id     bigint    NOT NULL,
    content      text,
    created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (comment_id)
);


CREATE TABLE facility
(
    facility_id  bigint      NOT NULL AUTO_INCREMENT,
    name         varchar(25) NOT NULL,
    path         varchar(255)         DEFAULT NULL,
    created_date timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (facility_id)
);


CREATE TABLE member
(
    member_id         bigint                                                       NOT NULL AUTO_INCREMENT,
    nickname          varchar(25)                                                  NOT NULL,
    profile_image_url varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci         DEFAULT NULL,
    email             varchar(25) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
    name              varchar(25)                                                  NOT NULL,
    status            varchar(25)                                                  NOT NULL,
    oauth_server_id   varchar(25)                                                  NOT NULL,
    oauth_server      varchar(25)                                                  NOT NULL,
    created_date      timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date      timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id)
);


CREATE TABLE owner
(
    owner_id     bigint      NOT NULL AUTO_INCREMENT,
    name         varchar(25) NOT NULL,
    image_path   varchar(255)         DEFAULT NULL,
    phone        varchar(25) NOT NULL,
    email        varchar(25)          DEFAULT NULL,
    platform     varchar(25) NOT NULL,
    status       varchar(25) NOT NULL,
    created_date timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (owner_id)
);


CREATE TABLE payment
(
    payment_id   bigint       NOT NULL AUTO_INCREMENT,
    imp_uid      varchar(255) NOT NULL,
    pay_method   varchar(25)  NOT NULL,
    price        int          NOT NULL,
    status       varchar(25)  NOT NULL,
    created_date timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (payment_id)
);


CREATE TABLE reservation
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


CREATE TABLE reservation_day
(
    reservation_day_id bigint      NOT NULL AUTO_INCREMENT,
    restaurant_id      bigint      NOT NULL,
    day                varchar(25) NOT NULL,
    time               time        NOT NULL,
    created_date       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_day_id)
);


CREATE TABLE restaurant
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


CREATE TABLE restaurant_image
(
    menu_id       bigint       NOT NULL AUTO_INCREMENT,
    restaurant_id bigint       NOT NULL,
    path          varchar(255) NOT NULL,
    type          varchar(25)  NOT NULL,
    created_date  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (menu_id)
);


CREATE TABLE restaurant_notification
(
    notification_id bigint      NOT NULL AUTO_INCREMENT,
    restaurant_id   bigint      NOT NULL,
    owner_id        bigint      NOT NULL,
    title           varchar(25) NOT NULL,
    content         text        NOT NULL,
    created_date    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id)
);


CREATE TABLE review
(
    review_id      bigint    NOT NULL AUTO_INCREMENT,
    member_id      bigint    NOT NULL,
    restaurant_id  bigint    NOT NULL,
    reservation_id bigint    NOT NULL,
    rate           tinyint   NOT NULL,
    comment        text,
    created_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (review_id)
);


CREATE TABLE review_image
(
    review_image_id bigint       NOT NULL AUTO_INCREMENT,
    review_id       bigint       NOT NULL,
    path            varchar(255) NOT NULL,
    created_date    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (review_image_id)
);


CREATE TABLE saved_restaurant
(
    member_id     bigint    NOT NULL,
    restaurant_id bigint    NOT NULL,
    created_date  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id, restaurant_id)
);



CREATE TABLE store_facility
(
    facility_id   bigint    NOT NULL,
    restaurant_id bigint    NOT NULL,
    created_date  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (facility_id, restaurant_id)
);