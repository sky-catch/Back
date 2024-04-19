package com.example.api.restaurantnotification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantNotificationDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
class RestaurantNotificationServiceTest {

    private final RestaurantMapper restaurantMapper;
    private final RestaurantNotificationService restaurantNotificationService;

    @Test
    @DisplayName("가게 공지사항을 생성하는 테스트")
    void test1() {
        // given
        long restaurantId = getTestRestaurantId();
        RestaurantNotificationDTO dto = RestaurantNotificationDTO.builder()
                .restaurantId(restaurantId)
                .ownerId(1L)
                .title("제목입니다.")
                .content("내용입니다.")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        // when
        long actual = restaurantNotificationService.createRestaurantNotification(dto);

        // then
        assertEquals(1, actual);
    }

    private long getTestRestaurantId() {
        RestaurantDTO dto = RestaurantDTO.builder()
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
                .lat(BigDecimal.valueOf(33.450701))
                .lng(BigDecimal.valueOf(126.570667))
                .build();
        restaurantMapper.save(dto);
        return dto.getRestaurantId();
    }
}