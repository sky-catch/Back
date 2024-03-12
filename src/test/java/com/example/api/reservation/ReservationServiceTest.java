package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.CANCEL;
import static com.example.api.reservation.ReservationStatus.DONE;
import static com.example.api.reservation.ReservationStatus.PLANNED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.reservation.ReservationController.GetMyReservationDTO;
import com.example.api.reservation.dto.GetReservationRes;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:truncate.sql")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private RestaurantMapper restaurantMapper;

    private RestaurantDTO restaurant;

    @BeforeEach
    void init() {
        restaurant = RestaurantDTO.builder()
                .ownerId(1L)
                .name("name")
                .category("category")
                .content("content")
                .phone("phone")
                .capacity(10)
                .openTime(LocalTime.of(10, 0, 0))
                .lastOrderTime(LocalTime.of(20, 0, 0))
                .address("address")
                .detailAddress("detailAddress")
                .build();
        restaurantMapper.save(restaurant);
    }

    // todo 테스트 보충하기

    @Test
    @DisplayName("방문 상태로 나의 예약 조회를 할 수 있다.")
    void test1() {
        // given
        for (int i = 1; i <= 15; i++) {
            ReservationStatus status = CANCEL;
            if (i <= 10) {
                status = PLANNED;
            }
            if (i <= 5) {
                status = DONE;
            }
            ReservationDTO dto = ReservationDTO.builder()
                    .restaurantId(restaurant.getRestaurantId())
                    .memberId(1L)
                    .reservationDayId(1L)
                    .paymentId(1L)
                    .time(LocalDateTime.now())
                    .numberOfPeople(2)
                    .memo("메모")
                    .status(status)
                    .build();
            reservationMapper.save(dto);
        }

        GetMyReservationDTO dto = GetMyReservationDTO.builder()
                .memberId(1L)
                .status(DONE)
                .build();

        // when
        List<GetReservationRes> expected = reservationService.getMyReservations(dto);

        // then
        assertAll(() -> {
            assertEquals(expected.size(), 5);
        });
    }

    // todo 예약 가능 시간 올바른지 테스트 보충하기
    @Test
    @DisplayName("새로운 예약을 생성하는 테스트")
    void test2() {
        // given
        ReservationDTO dto = ReservationDTO.builder()
                .restaurantId(restaurant.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .paymentId(1L)
                .time(LocalDateTime.of(2024, 1, 1, 15, 0, 0))
                .numberOfPeople(2)
                .memo("메모")
                .status(PLANNED)
                .build();

        // when
        long actual = reservationService.createReservation(dto);

        // then
        assertEquals(1L, actual);
    }

    @Test
    @DisplayName("예약 불가능한 시간에 예약하는 경우 예외 발생하는 테스트")
    void test3() {
        // given
        ReservationDTO dto = ReservationDTO.builder()
                .restaurantId(restaurant.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .paymentId(1L)
                .time(LocalDateTime.of(2024, 1, 1, 9, 0, 0))
                .numberOfPeople(2)
                .memo("메모")
                .status(PLANNED)
                .build();

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("예약 가능한 시간이 아닙니다.");
    }

    @Test
    @DisplayName("PLANNED 상태가 아닌 경우 예외 발생하는 테스트")
    void test4() {
        // given
        ReservationDTO dto = ReservationDTO.builder()
                .restaurantId(restaurant.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .paymentId(1L)
                .time(LocalDateTime.of(2024, 1, 1, 15, 0, 0))
                .numberOfPeople(2)
                .memo("메모")
                .status(DONE)
                .build();

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("잘못된 예약 상태입니다.");
    }
}