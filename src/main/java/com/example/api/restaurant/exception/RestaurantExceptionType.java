package com.example.api.restaurant.exception;

public enum RestaurantExceptionType {

    NOT_FOUND("존재하지 않는 식당입니다."),
    NOT_OWNER("식당 주인이 아닙니다."),
    CAN_CREATE_ONLY_ONE("식당은 한 개만 생성할 수 있습니다."),
    NOT_UNIQUE_NAME("식당 이름이 중복됩니다."),
    ;

    private final String message;

    RestaurantExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
