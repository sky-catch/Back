package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.CANCEL;
import static com.example.api.reservation.ReservationStatus.DONE;
import static com.example.api.reservation.ReservationStatus.PLANNED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.GetReservationRes;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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

    private RestaurantDTO testRestaurant;
    private final LocalTime openTime = LocalTime.of(10, 0, 0);
    private final LocalTime lastOrderTime = LocalTime.of(20, 0, 0);

    @BeforeEach
    void init() {
        restaurantMapper.deleteAll();

        testRestaurant = RestaurantDTO.builder()
                .ownerId(1L)
                .name("name")
                .category("category")
                .content("content")
                .phone("phone")
                .tablePersonMax(4)
                .tablePersonMin(2)
                .openTime(openTime)
                .lastOrderTime(lastOrderTime)
                .closeTime(LocalTime.of(22, 0, 0))
                .address("address")
                .detailAddress("detailAddress")
                .build();
        restaurantMapper.save(testRestaurant);
    }

    @AfterEach
    void cleanup() {
        restaurantMapper.deleteAll();
        reservationMapper.deleteAll();
    }

    // todo 테스트 보충하기

    @Test
    @DisplayName("방문 상태로 나의 예약을 조회할 수 있다.")
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
                    .restaurantId(testRestaurant.getRestaurantId())
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
                .restaurantId(testRestaurant.getRestaurantId())
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
                .restaurantId(testRestaurant.getRestaurantId())
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
                .restaurantId(testRestaurant.getRestaurantId())
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

    @Test
    @DisplayName("해당 날짜에 식당의 예약이 없다면 오픈 시간부터 주문 마감 시간까지 30분 단위로 예약 가능 시간을 반환하는 테스트")
    void test5() {
        // given
        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantDTO(testRestaurant)
                .numberOfPeople(2)
                .searchDate(LocalDate.now())
                .visitTime(openTime)
                .build();

        // when
        TimeSlots actual = reservationService.getAvailableTimeSlots(dto);

        // then
        assertThat(actual.getTimeSlots())
                .hasSize(21)
                .containsExactly(
                        TimeSlot.of(LocalTime.of(10, 0, 0)),
                        TimeSlot.of(LocalTime.of(10, 30, 0)),
                        TimeSlot.of(LocalTime.of(11, 0, 0)),
                        TimeSlot.of(LocalTime.of(11, 30, 0)),
                        TimeSlot.of(LocalTime.of(12, 0, 0)),
                        TimeSlot.of(LocalTime.of(12, 30, 0)),
                        TimeSlot.of(LocalTime.of(13, 0, 0)),
                        TimeSlot.of(LocalTime.of(13, 30, 0)),
                        TimeSlot.of(LocalTime.of(14, 0, 0)),
                        TimeSlot.of(LocalTime.of(14, 30, 0)),
                        TimeSlot.of(LocalTime.of(15, 0, 0)),
                        TimeSlot.of(LocalTime.of(15, 30, 0)),
                        TimeSlot.of(LocalTime.of(16, 0, 0)),
                        TimeSlot.of(LocalTime.of(16, 30, 0)),
                        TimeSlot.of(LocalTime.of(17, 0, 0)),
                        TimeSlot.of(LocalTime.of(17, 30, 0)),
                        TimeSlot.of(LocalTime.of(18, 0, 0)),
                        TimeSlot.of(LocalTime.of(18, 30, 0)),
                        TimeSlot.of(LocalTime.of(19, 0, 0)),
                        TimeSlot.of(LocalTime.of(19, 30, 0)),
                        TimeSlot.of(LocalTime.of(20, 0, 0))
                );
    }

    @Test
    @DisplayName("해당 날짜에 식당의 예약이 있다면 예약 시간을 제외한 예약 가능 시간을 반환하는 테스트")
    void test6() {
        // given
        for (int i = 0; i < 10; i++) {
            ReservationDTO dto = ReservationDTO.builder()
                    .restaurantId(testRestaurant.getRestaurantId())
                    .memberId(1L)
                    .reservationDayId(1L)
                    .paymentId(1L)
                    .time(LocalDateTime.of(LocalDate.now(), openTime.plusMinutes(i * 30)))
                    .numberOfPeople(2)
                    .memo("메모")
                    .status(PLANNED)
                    .build();
            reservationMapper.save(dto);

        }

        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantDTO(testRestaurant)
                .numberOfPeople(2)
                .searchDate(LocalDate.now())
                .visitTime(openTime)
                .build();

        // when
        TimeSlots actual = reservationService.getAvailableTimeSlots(dto);

        // then
        assertThat(actual.getTimeSlots())
                .hasSize(11)
                .containsExactly(
                        TimeSlot.of(LocalTime.of(15, 0, 0)),
                        TimeSlot.of(LocalTime.of(15, 30, 0)),
                        TimeSlot.of(LocalTime.of(16, 0, 0)),
                        TimeSlot.of(LocalTime.of(16, 30, 0)),
                        TimeSlot.of(LocalTime.of(17, 0, 0)),
                        TimeSlot.of(LocalTime.of(17, 30, 0)),
                        TimeSlot.of(LocalTime.of(18, 0, 0)),
                        TimeSlot.of(LocalTime.of(18, 30, 0)),
                        TimeSlot.of(LocalTime.of(19, 0, 0)),
                        TimeSlot.of(LocalTime.of(19, 30, 0)),
                        TimeSlot.of(LocalTime.of(20, 0, 0))
                );
    }
}