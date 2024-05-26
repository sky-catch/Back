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
    NOT_VALID_KOREAN_CITY("잘못된 KOREAN CITY입니다.", HttpStatus.BAD_REQUEST),
    NOT_VALID_HOT_PLACE("잘못된 핫플레이스입니다.", HttpStatus.BAD_REQUEST),
    NOT_VALID_CATEGORY("잘못된 카테고리입니다.", HttpStatus.BAD_REQUEST),
    NOT_VALID_TABLE_PERSON("식당 최소 인원이 최대 인원이 보다 큽니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
