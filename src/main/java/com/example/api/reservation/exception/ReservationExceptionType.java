package com.example.api.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationExceptionType {

    NOT_VALID_VISIT_TIME("방문 시간이 올바르지 않습니다."),
    NOT_FOUND("예약이 존재하지 않습니다."),
    NOT_VALID_STATUS("잘못된 예약 상태입니다."),
    OUTBOUND_PERSON("방문 인원 수가 잘못됐습니다."),
    RESERVATION_ON_HOLIDAY("휴일에는 예약할 수 없습니다."),
    NOT_AVAILABLE_DATE("예약 가능한 기간이 아닙니다."),
    ALREADY_EXISTS("해당 방문일의 방문 시간에 예약이 이미 존재합니다."),
    ;

    private final String message;
}
