package com.example.api.payment.exception;

import com.example.core.exception.CommonExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionType implements CommonExceptionType {

    NOT_FOUND("결제가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_PAID("결제 미완료", HttpStatus.BAD_GATEWAY),
    NOT_MATCH_PAYMENT_PRICE("결제 금액 위변조 의심", HttpStatus.BAD_GATEWAY),
    AMOUNT_OF_PAY_NOT_POSITIVE("결제 금액은 양수여야 합니다.", HttpStatus.BAD_REQUEST),
    REFUND_FAIL("환불 취소", HttpStatus.BAD_GATEWAY),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
