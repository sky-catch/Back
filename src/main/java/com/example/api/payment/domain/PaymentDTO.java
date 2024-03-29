package com.example.api.payment.domain;

import com.example.api.payment.exception.PaymentExceptionType;
import com.example.core.dto.BaseDTO;
import com.example.core.exception.SystemException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO extends BaseDTO {

    private long paymentId;
    private String impUid;
    private String payMethod;
    private int price;
    private String status;

    public void changeToPaid(String impUid) {
        this.impUid = impUid;
        this.status = PaymentStatus.PAID.getValue();
    }

    public static PaymentDTO ofReadyStatus(int amountToPay) {
        if (amountToPay < 0) {
            throw new SystemException(PaymentExceptionType.AMOUNT_OF_PAY_NOT_POSITIVE.getMessage());
        }

        return PaymentDTO.builder()
                .impUid("")
                .payMethod("")
                .price(amountToPay)
                .status(PaymentStatus.READY.getValue())
                .build();
    }
}