package com.example.api.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionType {

    NOT_FOUND("결제가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_PAID("결제 미완료", HttpStatus.BAD_GATEWAY),
    NOT_MATCH_PAYMENT_PRICE("결제 금액 위변조 의심", HttpStatus.BAD_GATEWAY),
    AMOUNT_OF_PAY_NOT_POSITIVE("결제 금액은 양수여야 합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;
}
