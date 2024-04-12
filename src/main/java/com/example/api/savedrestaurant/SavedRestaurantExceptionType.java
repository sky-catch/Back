package com.example.api.savedrestaurant;

import com.example.core.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SavedRestaurantExceptionType implements CommonExceptionType {

    ALREADY_EXISTS("해당 식당은 이미 저장하였습니다.", HttpStatus.CONFLICT),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
