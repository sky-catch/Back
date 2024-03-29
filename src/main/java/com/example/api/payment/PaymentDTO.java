package com.example.api.payment;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
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
        this.status = "OK";
    }
}