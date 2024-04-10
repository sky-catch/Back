package com.example.api.owner;

import static com.example.api.reservation.ReservationStatus.CANCEL;
import static com.example.api.reservation.ReservationStatus.DONE;
import static com.example.api.reservation.ReservationStatus.PLANNED;
import static com.example.core.oauth.domain.OauthServerType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.member.MemberDTO;
import com.example.api.payment.PaymentMapper;
import com.example.api.payment.domain.PaymentDTO;
import com.example.api.payment.domain.PaymentStatus;
import com.example.api.reservation.ReservationDTO;
import com.example.api.reservation.ReservationMapper;
import com.example.api.reservation.ReservationStatus;
import com.example.api.reservation.dto.request.ChangeReservationsStatusToNoShowReq;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.dto.HumanStatus;
import com.example.core.exception.SystemException;
import com.example.core.oauth.domain.OauthId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
@ActiveProfiles("test")
@Sql("classpath:truncate.sql")
class OwnerServiceTest {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    private final RestaurantMapper restaurantMapper;
    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;

    private final LocalTime openTime = LocalTime.of(10, 0, 0);
    private final LocalTime lastOrderTime = LocalTime.of(20, 0, 0);
    private final int tablePersonMax = 4;
    private final int tablePersonMin = 2;
    private final LocalDate notHoliday = LocalDate.of(2024, 3, 15); // FRIDAY
    private final LocalDateTime validVisitTime = LocalDateTime.of(notHoliday, openTime);

    @Test
    @DisplayName("새로운 사장을 생성하는 테스트")
    void test1() {
        // given
        OauthId oauthId = OauthId.builder()
                .oauthServerId(String.valueOf(1L))
                .oauthServerType(KAKAO)
                .build();
        MemberDTO dto = MemberDTO.builder()
                .nickname("testNickname")
                .profileImageUrl("testProfileImageUrl")
                .email("testEmail@test.com")
                .name("testName")
                .status(HumanStatus.ACTIVE)
                .oauthServerId(oauthId.oauthServerId())
                .oauthServer(oauthId.oauthServer())
                .build();

        // when
        long before = ownerMapper.findAll().size();
        ownerService.createOwner(dto, "101-01-00015");
        long actual = ownerMapper.findAll().size();

        // then
        assertEquals(before + 1, actual);
    }

    @Test
    @DisplayName("중복 사장 생성은 예외를 반환하는 테스트")
    void test2() {
        // given
        OauthId oauthId = OauthId.builder()
                .oauthServerId(String.valueOf(1L))
                .oauthServerType(KAKAO)
                .build();
        MemberDTO dto = MemberDTO.builder()
                .nickname("testNickname")
                .profileImageUrl("testProfileImageUrl")
                .email("testEmail@test.com")
                .name("testName")
                .status(HumanStatus.ACTIVE)
                .oauthServerId(oauthId.oauthServerId())
                .oauthServer(oauthId.oauthServer())
                .build();
        ownerService.createOwner(dto, "101-01-00015");

        // expected
        assertThatThrownBy(() -> ownerService.createOwner(dto, "101-01-00015"))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("사장의 중복 생성은 불가능합니다.");
    }

    @Test
    @DisplayName("예약 번호들로 예약 상태를 노쇼로 변경할 수 있다.")
    void changeReservationsToNoShowTest() {
        // given
        saveTestReservations(saveTestRestaurant());
        saveTestPayment();

        ChangeReservationsStatusToNoShowReq req = new ChangeReservationsStatusToNoShowReq(
                Arrays.asList(6L, 7L, 8L, 9L, 10L));

        // when
        ownerService.changeReservationsToNoShow(req);

        // then
        assertThat(reservationMapper.findAll())
                .extracting("status")
                .containsExactly(DONE, DONE, DONE, DONE, DONE, CANCEL, CANCEL, CANCEL, CANCEL, CANCEL, CANCEL, CANCEL,
                        CANCEL, CANCEL, CANCEL);
    }

    @Test
    @DisplayName("예약 상태가 잘못된 예약 번호들로 예약 상태를 노쇼로 변경할 경우 예외가 발생한다.")
    void changeReservationsToNoShowWithNotValidStatusTest() {
        // given
        saveTestReservations(saveTestRestaurant());
        saveTestPayment();

        ChangeReservationsStatusToNoShowReq req = new ChangeReservationsStatusToNoShowReq(
                Arrays.asList(1L, 2L, 3L, 4L, 5L));

        // expected
        assertThatThrownBy(() -> ownerService.changeReservationsToNoShow(req))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_VALID_STATUS.getMessage());
    }

    private RestaurantDTO saveTestRestaurant() {
        RestaurantDTO testRestaurant = RestaurantDTO.builder()
                .ownerId(1L)
                .name("name")
                .category("category")
                .content("content")
                .phone("phone")
                .tablePersonMax(tablePersonMax)
                .tablePersonMin(tablePersonMin)
                .openTime(openTime)
                .lastOrderTime(lastOrderTime)
                .closeTime(LocalTime.of(22, 0, 0))
                .address("address")
                .detailAddress("detailAddress")
                .build();
        restaurantMapper.save(testRestaurant);
        return testRestaurant;
    }

    private void saveTestReservations(RestaurantDTO testRestaurant) {
        for (int i = 1; i <= 15; i++) {
            ReservationStatus status = CANCEL;
            if (i <= 10) {
                status = PLANNED;
            }
            if (i <= 5) {
                status = DONE;
            }
            ReservationDTO dto = ReservationDTO.builder()
                    .restaurantId(testRestaurant.getRestaurantId())
                    .memberId(1L)
                    .paymentId(1L)
                    .time(validVisitTime)
                    .numberOfPeople(tablePersonMin)
                    .memo("메모")
                    .status(status)
                    .build();
            reservationMapper.save(dto);
        }
    }

    private void saveTestPayment() {
        PaymentDTO payment = PaymentDTO.builder()
                .impUid("testImpUid")
                .payMethod("card")
                .price(1000)
                .status(PaymentStatus.PAID.getValue())
                .build();
        paymentMapper.save(payment);
    }
}