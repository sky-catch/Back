package com.example.api.payment.service;

import static com.example.api.reservation.ReservationStatus.PLANNED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.member.MemberDTO;
import com.example.api.payment.PaymentMapper;
import com.example.api.payment.domain.PaymentDTO;
import com.example.api.payment.domain.PaymentStatus;
import com.example.api.payment.dto.PaymentCallbackRequest;
import com.example.api.payment.exception.PaymentExceptionType;
import com.example.api.reservation.ReservationDTO;
import com.example.api.reservation.ReservationMapper;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.core.exception.SystemException;
import com.example.core.payment.CorePaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@ActiveProfiles("test")
@Sql("classpath:truncate.sql")
class PaymentServiceTest {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final ReservationMapper reservationMapper;

    private PaymentDTO testPayment;
    private ReservationDTO testReservation;
    private final MemberDTO testMember = MemberDTO.builder()
            .memberId(1L)
            .email("testEmail")
            .name("test")
            .build();

    public PaymentServiceTest(PaymentMapper paymentMapper, ReservationMapper reservationMapper) {
        this.paymentMapper = paymentMapper;
        this.reservationMapper = reservationMapper;
        this.paymentService = new PaymentService(paymentMapper, reservationMapper,
                new CorePaymentService() {
                    @Override
                    public IamportResponse<Payment> getPaymentByImpUid(String impUid) {
                        Payment payment = new Payment();
                        ReflectionTestUtils.setField(payment, "status", PaymentStatus.PAID.getValue());
                        ReflectionTestUtils.setField(payment, "amount", BigDecimal.valueOf(testPayment.getPrice()));
                        ReflectionTestUtils.setField(payment, "imp_uid", "testImpUid");
                        ReflectionTestUtils.setField(payment, "buyer_name", testMember.getName());

                        IamportResponse<Payment> iamportResponse = new IamportResponse<>();
                        ReflectionTestUtils.setField(iamportResponse, "response", payment);
                        return iamportResponse;
                    }

                    @Override
                    public IamportResponse<Payment> cancelPaymentByImpUid(String impUid, int paymentAmount) {
                        return new IamportResponse<>();
                    }
                });
    }

    @BeforeEach
    void init() {
        testPayment = PaymentDTO.builder()
                .impUid("testImpUid")
                .payMethod("card")
                .price(1000)
                .status(PaymentStatus.PAID.getValue())
                .build();
        paymentMapper.save(testPayment);

        testReservation = ReservationDTO.builder()
                .restaurantId(1L)
                .memberId(1L)
                .paymentId(testPayment.getPaymentId())
                .time(LocalDateTime.of(2024, 1, 1, 10, 0, 0))
                .numberOfPeople(2)
                .memo("메모")
                .status(PLANNED)
                .build();
        reservationMapper.save(testReservation);
    }

    @Test
    @DisplayName("실제 결제와 DB의 결제를 검증하는 테스트")
    void paymentByCallbackTest() {
        // given
        PaymentCallbackRequest req = new PaymentCallbackRequest(testPayment.getImpUid(),
                testReservation.getReservationId());

        // when
        Payment actual = paymentService.paymentByCallback(testMember, req).getResponse();

        // then
        assertAll(() -> {
            assertEquals(testPayment.getPrice(), actual.getAmount().intValue());
            assertEquals(testMember.getName(), actual.getBuyerName());
        });
    }

    @Test
    @DisplayName("존재하지 않는 예약 ID로 결제 검증을 하면 예외 발생하는 테스트")
    void paymentByCallbackWithNotValidDomainIdTest() {
        // given
        long notExistsReservationId = 100L;
        PaymentCallbackRequest req = new PaymentCallbackRequest(testPayment.getImpUid(), notExistsReservationId);

        // expected
        assertThatThrownBy(() -> paymentService.paymentByCallback(testMember, req))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("내 예약이 아닌데 결제 검증을 하면 예외 발생하는 테스트")
    void paymentByCallbackWithNotMineTest() {
        // given
        ReservationDTO notMyReservation = ReservationDTO.builder()
                .restaurantId(1L)
                .memberId(2L)
                .paymentId(testPayment.getPaymentId())
                .time(LocalDateTime.of(2024, 1, 1, 11, 0, 0))
                .numberOfPeople(2)
                .memo("메모")
                .status(PLANNED)
                .build();
        reservationMapper.save(notMyReservation);
        PaymentCallbackRequest req = new PaymentCallbackRequest(testPayment.getImpUid(),
                notMyReservation.getReservationId());

        // expected
        assertThatThrownBy(() -> paymentService.paymentByCallback(testMember, req))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_MINE.getMessage());
    }

    @Test
    @DisplayName("실제 결제와 결제 DB의 금액이 다른 경우 예외 발생하는 테스트")
    void paymentByCallbackWithNotValidPaymentStatusTest() {
        // given
        PaymentDTO notValidPayment = PaymentDTO.builder()
                .impUid("testImpUid")
                .payMethod("card")
                .price(testPayment.getPrice() + 1000)
                .status(PaymentStatus.PAID.getValue())
                .build();
        paymentMapper.save(notValidPayment);
        ReservationDTO notMyReservation = ReservationDTO.builder()
                .restaurantId(1L)
                .memberId(1L)
                .paymentId(notValidPayment.getPaymentId())
                .time(LocalDateTime.of(2024, 1, 1, 11, 0, 0))
                .numberOfPeople(2)
                .memo("메모")
                .status(PLANNED)
                .build();
        reservationMapper.save(notMyReservation);
        PaymentCallbackRequest req = new PaymentCallbackRequest(notValidPayment.getImpUid(),
                notMyReservation.getReservationId());

        // expected
        assertThatThrownBy(() -> paymentService.paymentByCallback(testMember, req))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(PaymentExceptionType.NOT_MATCH_PAYMENT_PRICE.getMessage());
    }
}