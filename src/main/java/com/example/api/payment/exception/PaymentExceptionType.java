package com.example.api.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionType {

    NOT_FOUND("결제가 존재하지 않습니다."),
    NOT_PAID("결제 미완료"),
    NOT_MATCH_PAYMENT_PRICE("결제 금액 위변조 의심"),
    ;

    private final String message;
}
