package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.CANCEL;
import static com.example.api.reservation.ReservationStatus.DONE;
import static com.example.api.reservation.ReservationStatus.PLANNED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.reservation.ReservationController.GetMyReservationDTO;
import com.example.api.reservation.dto.GetReservationRes;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
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
                .capacity(1)
                .openTime(LocalTime.now().toString())
                .lastOrderTime(LocalTime.now().toString())
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
}