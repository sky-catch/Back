package com.example.api.restaurantimage.exception;

import com.example.core.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RestaurantImageExceptionType implements CommonExceptionType {

    NOT_SATISFIED_IMAGE_SIZE("식당 이미지는 1 ~ 10개 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCH_IMAGE_SIZE_WITH_IMAGE_TYPE_SIZE("식당 이미지와 식당 이미지 타입의 개수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_SATISFIED_IMAGE_TYPE_REQUIREMENT("대표 식당 이미지는 한 개여야 합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
