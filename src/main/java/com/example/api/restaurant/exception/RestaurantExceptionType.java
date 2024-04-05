package com.example.api.restaurant.exception;

import com.example.core.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RestaurantExceptionType implements CommonExceptionType {

    NOT_FOUND("존재하지 않는 식당입니다.", HttpStatus.NOT_FOUND),
    NOT_OWNER("식당 주인이 아닙니다.", HttpStatus.BAD_REQUEST),
    CAN_CREATE_ONLY_ONE("식당은 한 개만 생성할 수 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_UNIQUE_NAME("식당 이름이 중복됩니다.", HttpStatus.BAD_REQUEST),
    NOT_VALID_SAVED_COUNT("잘못된 식당 저장 개수입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
