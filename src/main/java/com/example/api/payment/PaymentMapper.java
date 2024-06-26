package com.example.api.payment;

import com.example.api.payment.domain.PaymentDTO;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    Optional<PaymentDTO> getPaymentById(long paymentId);

    void deleteById(long paymentId);

    void update(PaymentDTO payment);

    void save(PaymentDTO payment);
}