TRUNCATE TABLE RESERVATION_AVAILABLE_DATE RESTART IDENTITY;
TRUNCATE TABLE HOLIDAY RESTART IDENTITY;
TRUNCATE TABLE RESTAURANT_NOTIFICATION RESTART IDENTITY;
TRUNCATE TABLE RESERVATION RESTART IDENTITY;
TRUNCATE TABLE RESTAURANT RESTART IDENTITY;
TRUNCATE TABLE OWNER RESTART IDENTITY;
TRUNCATE TABLE MEMBER RESTART IDENTITY;
TRUNCATE TABLE FACILITY RESTART IDENTITY;
TRUNCATE TABLE STORE_FACILITY RESTART IDENTITY;
TRUNCATE TABLE SAVED_RESTAURANT RESTART IDENTITY;
TRUNCATE TABLE PAYMENT RESTART IDENTITY;
TRUNCATE TABLE RESERVATION_ALARM RESTART IDENTITY;
TRUNCATE TABLE ALARM RESTART IDENTITY;
TRUNCATE TABLE REVIEW RESTART IDENTITY;
TRUNCATE TABLE RESTAURANT_IMAGE RESTART IDENTITY;


ALTER TABLE HOLIDAY
    ALTER COLUMN HOLIDAY_ID RESTART WITH 1;
ALTER TABLE RESTAURANT_NOTIFICATION
    ALTER COLUMN NOTIFICATION_ID RESTART WITH 1;
ALTER TABLE RESERVATION
    ALTER COLUMN RESERVATION_ID RESTART WITH 1;
ALTER TABLE RESTAURANT
    ALTER COLUMN RESTAURANT_ID RESTART WITH 1;
ALTER TABLE OWNER
    ALTER COLUMN OWNER_ID RESTART WITH 1;
ALTER TABLE MEMBER
    ALTER COLUMN MEMBER_ID RESTART WITH 1;
ALTER TABLE FACILITY
    ALTER COLUMN FACILITY_ID RESTART WITH 1;
ALTER TABLE RESERVATION_AVAILABLE_DATE
    ALTER COLUMN RESERVATION_AVAILABLE_DATE_ID RESTART WITH 1;
ALTER TABLE PAYMENT
    ALTER COLUMN PAYMENT_ID RESTART WITH 1;
ALTER TABLE RESERVATION_ALARM
    ALTER COLUMN RESERVATION_ALARM_ID RESTART WITH 1;
ALTER TABLE ALARM
    ALTER COLUMN ALARM_ID RESTART WITH 1;
ALTER TABLE REVIEW
    ALTER COLUMN REVIEW_ID RESTART WITH 1;
ALTER TABLE RESTAURANT_IMAGE
    ALTER COLUMN RESTAURANT_IMAGE_ID RESTART WITH 1;