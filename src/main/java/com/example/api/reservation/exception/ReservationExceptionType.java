package com.example.api.reservation.exception;

import com.example.core.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationExceptionType implements CommonExceptionType {

    NOT_VALID_VISIT_TIME("방문 시간이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("예약이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_VALID_STATUS("잘못된 예약 상태입니다.", HttpStatus.BAD_REQUEST),
    OUTBOUND_PERSON("방문 인원 수가 잘못됐습니다.", HttpStatus.BAD_REQUEST),
    RESERVATION_ON_HOLIDAY("휴일에는 예약할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_AVAILABLE_DATE("예약 가능한 기간이 아닙니다.", HttpStatus.BAD_REQUEST),
    NOT_MINE("내 예약이 아닙니다.", HttpStatus.BAD_REQUEST),
    NOT_VALID_MINUTES("방문 시간은 정각 또는 30분 단위입니다.", HttpStatus.BAD_REQUEST),
    TIME_DUPLICATE("해당 시간에 예약이 존재합니다.", HttpStatus.CONFLICT),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
