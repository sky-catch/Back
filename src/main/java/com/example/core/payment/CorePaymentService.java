package com.example.core.payment;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface CorePaymentService {

    IamportResponse<Payment> getPaymentByImpUid(String impUid);

    IamportResponse<Payment> cancelPaymentByImpUid(String impUid, int paymentAmount);
}
