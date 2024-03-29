package com.example.api.payment.service;

import com.example.api.payment.PaymentDTO;
import com.example.api.payment.PaymentMapper;
import com.example.api.payment.dto.PaymentCallbackRequest;
import com.example.api.payment.exception.PaymentExceptionType;
import com.example.api.reservation.ReservationDTO;
import com.example.api.reservation.ReservationMapper;
import com.example.api.reservation.exception.ReservationExceptionType;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final String PAID = "paid";

    private final PaymentMapper paymentMapper;
    private final ReservationMapper reservationMapper;
    private final IamportClient iamportClient;

    // todo 테스트 추가하기
    @Transactional
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        try {
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getImpUid());
            log.info("iamportResponse = {}", iamportResponse.toString());

            ReservationDTO reservation = reservationMapper.getReservation(request.getReservationId())
                    .orElseThrow(() -> new SystemException(ReservationExceptionType.NOT_FOUND.getMessage()));
            PaymentDTO payment = paymentMapper.getPaymentById(reservation.getPaymentId())
                    .orElseThrow(() -> new SystemException(PaymentExceptionType.NOT_FOUND.getMessage()));

            validPaymentStatus(iamportResponse, reservation, payment);
            validAmountOfPayment(payment, iamportResponse, reservation);

            payment.changeToPaid(request.getImpUid());
            paymentMapper.update(payment);

            return iamportResponse;
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validPaymentStatus(IamportResponse<Payment> iamportResponse, ReservationDTO reservation,
                                    PaymentDTO payment) {
        if (!isPaid(iamportResponse)) {
            reservationMapper.deleteById(reservation.getReservationId());
            paymentMapper.deleteById(payment.getPaymentId());

            throw new SystemException(PaymentExceptionType.NOT_PAID.getMessage());
        }
    }

    private boolean isPaid(IamportResponse<Payment> iamportResponse) {
        return iamportResponse.getResponse().getStatus().equals(PAID);
    }

    private void validAmountOfPayment(PaymentDTO payment, IamportResponse<Payment> iamportResponse,
                                      ReservationDTO reservation)
            throws IamportResponseException, IOException {
        int expectedPaymentPrice = payment.getPrice();
        int actualPaymentPrice = iamportResponse.getResponse().getAmount().intValue();

        if (expectedPaymentPrice != actualPaymentPrice) {
            reservationMapper.deleteById(reservation.getReservationId());
            paymentMapper.deleteById(payment.getPaymentId());

            iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true,
                    new BigDecimal(actualPaymentPrice)));

            throw new SystemException(PaymentExceptionType.NOT_MATCH_PAYMENT_PRICE.getMessage());
        }
    }
}