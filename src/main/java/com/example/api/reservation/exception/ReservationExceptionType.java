package com.example.api.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationExceptionType {

    NOT_VALID_VISIT_TIME("방문 시간이 올바르지 않습니다."),
    NOT_FOUND("예약이 존재하지 않습니다."),
    ;

    private final String message;
}
