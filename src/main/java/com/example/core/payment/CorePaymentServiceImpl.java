package com.example.core.payment;

import com.example.core.exception.SystemException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorePaymentServiceImpl implements CorePaymentService {

    private final IamportClient iamportClient;

    @Override
    @Transactional
    public IamportResponse<Payment> getPaymentByImpUid(String impUid) {
        try {
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
            log.info("getPaymentByImpUid = {}", iamportResponse.toString());
            return iamportResponse;
        } catch (IamportResponseException | IOException e) {
            throw new SystemException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    @Override
    @Transactional
    public IamportResponse<Payment> cancelPaymentByImpUid(String impUid, int paymentAmount) {
        try {
            IamportResponse<Payment> iamportResponse = iamportClient.cancelPaymentByImpUid(
                    new CancelData(impUid, true, new BigDecimal(paymentAmount)));
            log.info("cancelPaymentByImpUid = {}", iamportResponse);
            return iamportResponse;
        } catch (IamportResponseException | IOException e) {
            throw new SystemException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
