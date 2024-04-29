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
    restaurant_id    bigint         NOT NULL AUTO_INCREMENT,
    owner_id         bigint         NOT NULL UNIQUE,
    name             varchar(25)    NOT NULL UNIQUE,
    category         varchar(25)    NOT NULL,
    content          text,
    phone            varchar(25)    NOT NULL,
    table_person_max int            NOT NULL,
    table_person_min int            NOT NULL,
    open_time        time           NOT NULL,
    last_order_time  time           NOT NULL,
    close_time       time           NOT NULL,
    address          varchar(25)    NOT NULL,
    hot_place        varchar(25),
    detail_address   varchar(50)    NOT NULL,
    lat              decimal(16, 7) NOT NULL,
    lng              decimal(17, 7) NOT NULL,
    lunch_price      int                     DEFAULT NULL,
    dinner_price     int                     DEFAULT NULL,
    saved_count      bigint                  DEFAULT NULL,
    review_count     bigint                  DEFAULT NULL,
    review_avg       float                   DEFAULT NULL,
    created_date     timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date     timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (restaurant_id)
);


CREATE TABLE RESERVATION
(
    reservation_id   bigint      NOT NULL AUTO_INCREMENT,
    restaurant_id    bigint      NOT NULL,
    member_id        bigint      NOT NULL,
    payment_id       bigint      NOT NULL,
    time             timestamp   NOT NULL,
    number_of_people int         NOT NULL,
    memo             varchar(255)         DEFAULT NULL,
    status           varchar(25) NOT NULL,
    created_date     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_id)
);

CREATE TABLE RESTAURANT_IMAGE
(
    restaurant_image_id bigint       NOT NULL AUTO_INCREMENT,
    restaurant_id       bigint       NOT NULL,
    path                varchar(255) NOT NULL,
    type                varchar(25)  NOT NULL,
    created_date        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (restaurant_image_id)
);

CREATE TABLE restaurant_notification
(
    notification_id bigint      NOT NULL AUTO_INCREMENT,
    restaurant_id   bigint      NOT NULL,
    owner_id        bigint      NOT NULL,
    title           varchar(25) NOT NULL,
    content         text        NOT NULL,
    start_date      date        NOT NULL,
    end_date        date        NOT NULL,
    created_date    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (notification_id)
);

CREATE TABLE OWNER
(
    owner_id                     bigint      NOT NULL AUTO_INCREMENT,
    name                         varchar(25) NOT NULL,
    image_path                   varchar(255)         DEFAULT NULL,
    email                        varchar(25)          DEFAULT NULL,
    oauth_server                 varchar(25) NOT NULL,
    status                       varchar(25) NOT NULL,
    business_registration_number varchar(25) NOT NULL,
    created_date                 timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date                 timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (owner_id)
);

CREATE TABLE facility
(
    facility_id  bigint      not null auto_increment,
    name         varchar(25) not null,
    en_name      varchar(25) not null,
    path         varchar(255) null,
    created_date timestamp   not null default CURRENT_TIMESTAMP,
    updated_date timestamp   not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (facility_id)
);

CREATE TABLE store_facility
(
    facility_id   bigint    not null,
    restaurant_id bigint    not null,
    created_date  timestamp          default CURRENT_TIMESTAMP not null,
    updated_date  timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (facility_id, restaurant_id)
);


CREATE TABLE review
(
    review_id      bigint auto_increment
        primary key,
    member_id      bigint                              not null,
    restaurant_id  bigint                              not null,
    reservation_id bigint                              not null,
    rate           tinyint                             not null,
    comment        text null,
    created_date   timestamp default CURRENT_TIMESTAMP not null,
    updated_date   timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);


CREATE TABLE HOLIDAY
(
    holiday_id    bigint      NOT NULL AUTO_INCREMENT,
    restaurant_id bigint      NOT NULL,
    day           varchar(25) NOT NULL,
    created_date  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (holiday_id)
);

CREATE TABLE RESERVATION_AVAILABLE_DATE
(
    reservation_available_date_id bigint    NOT NULL AUTO_INCREMENT,
    restaurant_id                 bigint    NOT NULL,
    begin_date                    date      NOT NULL,
    end_date                      date      NOT NULL,
    created_date                  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date                  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_available_date_id)
);

CREATE TABLE SAVED_RESTAURANT
(
    member_id     bigint    NOT NULL,
    restaurant_id bigint    NOT NULL,
    created_date  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id, restaurant_id)
);

CREATE TABLE PAYMENT
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

create table RESERVATION_ALARM
(
    reservation_alarm_id bigint      NOT NULL auto_increment,
    reservation_id       bigint      not null,
    type                 varchar(25) not null,
    schedule_time        timestamp   not null,
    created_date         timestamp   not null default CURRENT_TIMESTAMP,
    updated_date         timestamp   not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_alarm_id)
);

create table ALARM
(
    alarm_id         bigint      NOT NULL auto_increment,
    member_id        bigint      not null,
    restaurant_id    bigint      not null,
    type             varchar(25) not null,
    message          varchar(255) null,
    reservation_time timestamp null,
    review_id        bigint null,
    is_new           tinyint     not null,
    created_date     timestamp   not null default CURRENT_TIMESTAMP,
    updated_date     timestamp   not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    PRIMARY KEY (alarm_id)
);
