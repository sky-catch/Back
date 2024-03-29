package com.example.api.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    PAID("paid", "결제완료"),
    READY("ready", "결제대기"),
    CANCELLED("cancelled", "결제취소"),
    FAILED("failed", "결제실패"),
    ;

    private final String value;
    private final String mean;
}
