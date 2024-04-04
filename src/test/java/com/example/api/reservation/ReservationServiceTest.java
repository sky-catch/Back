package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.CANCEL;
import static com.example.api.reservation.ReservationStatus.DONE;
import static com.example.api.reservation.ReservationStatus.PLANNED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.api.holiday.Day;
import com.example.api.holiday.HolidayDTO;
import com.example.api.holiday.HolidayMapper;
import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.MyDetailReservationDTO;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.request.CreateReservationReq;
import com.example.api.reservation.dto.response.GetReservationRes;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.api.reservationavailabledate.ReservationAvailableDateMapper;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    @Autowired
    private HolidayMapper holidayMapper;
    @Autowired
    private ReservationAvailableDateMapper reservationAvailableDateMapper;

    private RestaurantDTO testRestaurant;
    private final LocalTime openTime = LocalTime.of(10, 0, 0);
    private final LocalTime lastOrderTime = LocalTime.of(20, 0, 0);
    private final int tablePersonMax = 4;
    private final int tablePersonMin = 2;
    private final int underTablePersonMin = tablePersonMin - 1;
    private final int overTablePersonMax = tablePersonMax + 1;
    private final LocalDate notHoliday = LocalDate.of(2024, 3, 15); // FRIDAY
    private final LocalDate holiday = LocalDate.of(2024, 3, 11); // MONDAY
    private final LocalDateTime validVisitTime = LocalDateTime.of(notHoliday, openTime);
    private final LocalDate availableBeginDate = LocalDate.of(2024, 1, 1);
    private final LocalDate availableEndDate = LocalDate.of(2024, 12, 31);
    private final LocalDate notAvailableDate = availableBeginDate.minusDays(1); // 예약 가능일이 아님
    private final LocalDateTime notValidVisitTime = LocalDateTime.of(notAvailableDate, openTime);

    @BeforeEach
    void init() {
        restaurantMapper.deleteAll();

        testRestaurant = RestaurantDTO.builder()
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

        holidayMapper.saveAll(getMondayAndTuesdayHolidays());

        reservationAvailableDateMapper.save(getTestReservationAvailableDate(testRestaurant.getRestaurantId()));
    }

    @AfterEach
    void cleanup() {
        restaurantMapper.deleteAll();
        reservationMapper.deleteAll();
        holidayMapper.deleteAll();
        reservationAvailableDateMapper.deleteAll();
    }

    @Test
    @DisplayName("방문 상태로 나의 예약을 조회할 수 있다.")
    void get_my_reservations_test() {
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
                    .time(validVisitTime)
                    .numberOfPeople(tablePersonMin)
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

    @Test
    @DisplayName("새로운 예약을 생성하는 테스트")
    void create_reservation() {
        // given
        CreateReservationDTO dto = CreateReservationDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .time(validVisitTime)
                .numberOfPeople(tablePersonMin)
                .memo("메모")
                .status(PLANNED)
                .build();

        // when
        reservationService.createReservation(dto);

        // then
        int actual = restaurantMapper.findAll().size();
        assertEquals(1L, actual);
    }

    @Test
    @DisplayName("휴일이 없는 식당에 새로운 예약을 생성하는 테스트")
    void create_reservation_with_no_holiday() {
        // given
        RestaurantDTO testRestaurant2 = RestaurantDTO.builder()
                .ownerId(2L)
                .name("name2")
                .category("category2")
                .content("content2")
                .phone("phone2")
                .tablePersonMax(tablePersonMax)
                .tablePersonMin(tablePersonMin)
                .openTime(openTime)
                .lastOrderTime(lastOrderTime)
                .closeTime(LocalTime.of(22, 0, 0))
                .address("address2")
                .detailAddress("detailAddress2")
                .build();
        restaurantMapper.save(testRestaurant2);
        reservationAvailableDateMapper.save(getTestReservationAvailableDate(testRestaurant2.getRestaurantId()));

        CreateReservationDTO dto = CreateReservationDTO.builder()
                .restaurantId(testRestaurant2.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .time(LocalDateTime.of(LocalDate.now(), openTime))
                .numberOfPeople(tablePersonMin)
                .memo("메모")
                .status(PLANNED)
                .build();

        // when
        reservationService.createReservation(dto);

        // then
        int actual = restaurantMapper.findAll().size();
        assertEquals(2L, actual);
    }

    @Test
    @DisplayName("예약 상태가 PLANNED가 아닌 경우 예외 발생하는 테스트")
    void createReservation_with_not_valid_status() {
        // given
        CreateReservationDTO dto = CreateReservationDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .time(validVisitTime)
                .numberOfPeople(tablePersonMin)
                .memo("메모")
                .status(DONE)
                .build();

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_VALID_STATUS.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {underTablePersonMin, overTablePersonMax})
    @DisplayName("방문 인원수가 잘못된 경우 예외 발생하는 테스트")
    void createReservation_with_outbound_table_person(int outboundTablePerson) {
        // given
        CreateReservationDTO dto = CreateReservationDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .memberId(1L)
                .reservationDayId(1L)
                .time(validVisitTime)
                .numberOfPeople(outboundTablePerson)
                .memo("메모")
                .status(PLANNED)
                .build();

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.OUTBOUND_PERSON.getMessage());
    }

    @Test
    @DisplayName("휴일에 예약을 하는 경우 예외 발생하는 테스트")
    void createReservation_with_holiday() {
        // given
        LocalDateTime notValidVisitDateTime = LocalDateTime.of(holiday, openTime);
        CreateReservationReq req = new CreateReservationReq(notValidVisitDateTime, tablePersonMin, "메모", 1000);
        CreateReservationDTO dto = CreateReservationDTO.of(testRestaurant.getRestaurantId(), 1L,
                req);

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.RESERVATION_ON_HOLIDAY.getMessage());
    }

    @Test
    @DisplayName("방문 시간이 잘못된 예약을 하는 경우 예외 발생하는 테스트")
    void createReservation_with_not_valid_visit_time() {
        // given
        LocalTime notOpenTime = openTime.minusMinutes(1);
        LocalDateTime notValidVisitDateTime = LocalDateTime.of(notHoliday, notOpenTime);
        CreateReservationReq req = new CreateReservationReq(notValidVisitDateTime, tablePersonMin, "메모", 1000);
        CreateReservationDTO dto = CreateReservationDTO.of(testRestaurant.getRestaurantId(), 1L,
                req);

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_VALID_VISIT_TIME.getMessage());
    }

    @Test
    @DisplayName("방문일이 잘못된 경우 예외 발생하는 테스트")
    void createReservation_with_not_valid_visit_date() {
        // given
        CreateReservationReq req = new CreateReservationReq(notValidVisitTime, tablePersonMin, "메모", 1000);
        CreateReservationDTO dto = CreateReservationDTO.of(testRestaurant.getRestaurantId(), 1L,
                req);

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_AVAILABLE_DATE.getMessage());
    }

    @Test
    @DisplayName("방문일의 방문 시간에 예약이 이미 존재하는 경우 예외 발생하는 테스트")
    void createReservation_with_duplicate() {
        // given
        CreateReservationReq req = new CreateReservationReq(validVisitTime, tablePersonMin, "메모", 1000);
        CreateReservationDTO dto = CreateReservationDTO.of(testRestaurant.getRestaurantId(), 1L,
                req);
        reservationService.createReservation(dto);

        // expected
        assertThatThrownBy(() -> reservationService.createReservation(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.ALREADY_EXISTS_AT_TIME.getMessage());
    }

    @Test
    @DisplayName("방문일에 식당의 예약이 없고, 방문일이 휴일이 아니고, 방문 시간이 오픈 시간 ~ 주문 마감 시간인 경우 "
            + "방문 시간 ~ 주문 마감 시간까지 30분 단위로 예약 가능 시간을 반환하는 테스트")
    void get_available_time_slots_with_all() {
        // given
        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .numberOfPeople(tablePersonMin)
                .searchDate(notHoliday)
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
    @DisplayName("방문일이 휴일이 아니고, 방문 시간이 오픈 시간 ~ 주문 마감 시간이지만, 방문일에 식당의 예약이 존재하는 경우 "
            + "예약 시간을 제외한 예약 가능 시간을 반환하는 테스트")
    void get_available_time_slots_with_not_include_reservation_time() {
        // given
        for (int i = 0; i < 10; i++) {
            ReservationDTO dto = ReservationDTO.builder()
                    .restaurantId(testRestaurant.getRestaurantId())
                    .memberId(1L)
                    .reservationDayId(1L)
                    .paymentId(1L)
                    .time(LocalDateTime.of(notHoliday, openTime.plusMinutes(i * 30)))
                    .numberOfPeople(tablePersonMin)
                    .memo("메모")
                    .status(PLANNED)
                    .build();
            reservationMapper.save(dto);
        }

        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .numberOfPeople(tablePersonMin)
                .searchDate(notHoliday)
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

    @Test
    @DisplayName("방문 시간이 오픈 시간 ~ 주문 마감 시간이지만 방문일이 휴일인 경우 빈 리스트를 반환하는 테스트")
    void get_available_time_slots_with_holiday() {
        // given
        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .numberOfPeople(tablePersonMin)
                .searchDate(holiday)
                .visitTime(openTime)
                .build();

        // when
        TimeSlots actual = reservationService.getAvailableTimeSlots(dto);

        // then
        assertTrue(actual.getTimeSlots().isEmpty());
    }

    @Test
    @DisplayName("방문일이 휴일이 아니고, 방문 시간이 오픈 시간 ~ 주문 마감 시간이 아닌 경우 "
            + "예외 발생하는 테스트")
    void get_available_time_slots_with_not_valid_visit_time() {
        // given
        LocalTime notOpenTime = openTime.minusMinutes(1);
        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .numberOfPeople(tablePersonMin)
                .searchDate(notHoliday)
                .visitTime(notOpenTime)
                .build();

        // expected
        assertThatThrownBy(() -> reservationService.getAvailableTimeSlots(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_VALID_VISIT_TIME.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {underTablePersonMin, overTablePersonMax})
    @DisplayName("방문일이 휴일이 아니고, 방문 시간이 오픈 시간 ~ 주문 마감 시간이고, 예약 가능 인원을 벗어난 경우 "
            + "빈 리스트를 반환하는 테스트")
    void get_available_time_slots_with_outbound_table_person(int outboundNumberOfPeople) {
        // given
        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantId(testRestaurant.getRestaurantId())
                .numberOfPeople(outboundNumberOfPeople)
                .searchDate(notHoliday)
                .visitTime(openTime)
                .build();

        // when
        TimeSlots actual = reservationService.getAvailableTimeSlots(dto);

        // then
        assertTrue(actual.getTimeSlots().isEmpty());
    }

    @Test
    @DisplayName("예약 ID로 나의 예약 상세 내용을 조회할 수 있다.")
    void getMyDetailReservationByIdTest() {
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
                    .time(validVisitTime)
                    .numberOfPeople(tablePersonMin)
                    .memo("메모")
                    .status(status)
                    .build();
            reservationMapper.save(dto);
        }

        // when
        MyDetailReservationDTO expected = reservationService.getMyDetailReservationById(1L, 1L);

        // then
        assertAll(() -> {
            assertEquals(expected.getRestaurant().getName(), testRestaurant.getName());
            assertEquals(expected.getStatus(), DONE);
        });
    }

    @Test
    @DisplayName("존재하지 않는 예약 ID로 나의 예약 상세 내용을 조회할 경우 예외 발생하는 테스트")
    void getMyDetailReservationByIdWithNotExistsReservationIdTest() {
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
                    .time(validVisitTime)
                    .numberOfPeople(tablePersonMin)
                    .memo("메모")
                    .status(status)
                    .build();
            reservationMapper.save(dto);
        }

        long notExistsReservationId = 100L;

        // expected
        assertThatThrownBy(() -> reservationService.getMyDetailReservationById(notExistsReservationId, 1L))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("예약 ID로 나의 예약 상세 내용을 조회할 때 나의 예약이 아닌 경우 예외 발생하는 테스트")
    void getMyDetailReservationByIdWithNotMyReservationTest() {
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
                    .time(validVisitTime)
                    .numberOfPeople(tablePersonMin)
                    .memo("메모")
                    .status(status)
                    .build();
            reservationMapper.save(dto);
        }
        long notMyReservationMemberId = 100L;

        // expected
        assertThatThrownBy(() -> reservationService.getMyDetailReservationById(1L, notMyReservationMemberId))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(ReservationExceptionType.NOT_MINE.getMessage());
    }

    @Test
    @DisplayName("예약 ID로 나의 예약 상세 내용을 조회할 수 있다.")
    void getMyDetailReservationByIdTest() {
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
                    .time(validVisitTime)
                    .numberOfPeople(tablePersonMin)
                    .memo("메모")
                    .status(status)
                    .build();
            reservationMapper.save(dto);
        }

        // when
        MyDetailReservationDTO expected = reservationService.getMyDetailReservationById(1L, 1L);

        // then
        assertAll(() -> {
            assertEquals(expected.getRestaurant().getName(), testRestaurant.getName());
            assertEquals(expected.getStatus(), DONE);
        });
    }

    private List<HolidayDTO> getMondayAndTuesdayHolidays() {
        HolidayDTO monday = HolidayDTO.builder().restaurantId(testRestaurant.getRestaurantId()).day(Day.MONDAY).build();
        HolidayDTO tuesday = HolidayDTO.builder().restaurantId(testRestaurant.getRestaurantId()).day(Day.TUESDAY)
                .build();
        return Arrays.asList(monday, tuesday);
    }

    private ReservationAvailableDateDTO getTestReservationAvailableDate(long restaurantId) {
        return ReservationAvailableDateDTO.builder()
                .restaurantId(restaurantId)
                .beginDate(availableBeginDate)
                .endDate(availableEndDate)
                .build();
    }
}