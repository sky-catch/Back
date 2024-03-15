package com.example.api.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationExceptionType {

    NOT_VALID_NUMBER_OF_PEOPLE("방문 인원이 올바르지 않습니다."),
    NOT_VALID_VISIT_TIME("방문 시간이 올바르지 않습니다."),
    ;

    private final String message;
}
