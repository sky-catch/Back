package com.example.api.payment;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    Optional<PaymentDTO> getPaymentById(long paymentId);

    void deleteById(long paymentId);

    void update(PaymentDTO payment);

    void deleteAll();
}