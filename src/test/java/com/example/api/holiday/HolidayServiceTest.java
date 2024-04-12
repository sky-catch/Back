package com.example.api.holiday;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.holiday.exception.HolidayExceptionType;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
class HolidayServiceTest {

    private final HolidayService holidayService;
    private final HolidayMapper holidayMapper;
    private final RestaurantMapper restaurantMapper;

    private RestaurantDTO testRestaurant;

    @BeforeEach
    void init() {
        testRestaurant = RestaurantDTO.builder()
                .ownerId(1L)
                .name("name")
                .category("category")
                .content("content")
                .phone("phone")
                .tablePersonMax(4)
                .tablePersonMin(2)
                .openTime(LocalTime.now())
                .lastOrderTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .address("address")
                .detailAddress("detailAddress")
                .build();
        restaurantMapper.save(testRestaurant);
    }

    @Test
    @DisplayName("새로운 휴무일인 경우 휴무일을 생성하는 테스트")
    void test1() {
        // given
        long restaurantId = testRestaurant.getRestaurantId();
        Days days = Days.of(Arrays.asList(Day.MONDAY.getValue(), Day.TUESDAY.getValue()));
        long before = holidayMapper.findAll().size();

        // when
        holidayService.createHolidays(restaurantId, days);

        // then
        long after = holidayMapper.findAll().size();
        assertEquals(before + 2, after);
    }

    @Test
    @DisplayName("이미 설정한 휴무일인 경우 예외 발생하는 테스트")
    void test2() {
        // given
        long restaurantId = testRestaurant.getRestaurantId();
        HolidayDTO dto1 = HolidayDTO.builder()
                .restaurantId(restaurantId)
                .day(Day.MONDAY)
                .build();
        HolidayDTO dto2 = HolidayDTO.builder()
                .restaurantId(restaurantId)
                .day(Day.TUESDAY)
                .build();
        HolidayDTO dto3 = HolidayDTO.builder()
                .restaurantId(restaurantId)
                .day(Day.WEDNESDAY)
                .build();
        List<HolidayDTO> holidayDTOs = Arrays.asList(dto1, dto2, dto3);
        holidayMapper.saveAll(holidayDTOs);
        Days days = Days.of(Arrays.asList(Day.MONDAY.getValue(), Day.THURSDAY.getValue()));

        // when
        assertThatThrownBy(() -> holidayService.createHolidays(restaurantId, days))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(HolidayExceptionType.ALREADY_EXISTS.getMessage());
    }
}