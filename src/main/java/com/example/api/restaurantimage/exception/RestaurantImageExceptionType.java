package com.example.api.restaurantimage.exception;

import lombok.Getter;

@Getter
public enum RestaurantImageExceptionType {

    NOT_SATISFIED_IMAGE_SIZE("식당 이미지는 1 ~ 10개 사이여야 합니다."),
    NOT_MATCH_IMAGE_SIZE_WITH_IMAGE_TYPE_SIZE("식당 이미지와 식당 이미지 타입의 개수가 일치하지 않습니다."),
    NOT_SATISFIED_IMAGE_TYPE_REQUIREMENT("대표 식당 이미지는 한 개여야 합니다."),
    ;

    private final String message;

    RestaurantImageExceptionType(String message) {
        this.message = message;
    }
}
