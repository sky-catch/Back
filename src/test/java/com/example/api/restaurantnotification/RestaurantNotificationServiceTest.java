package com.example.api.restaurantnotification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantNotificationDTO;
import java.time.LocalDate;
import java.time.LocalTime;
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
class RestaurantNotificationServiceTest {

    @Autowired
    private RestaurantMapper restaurantMapper;
    @Autowired
    private RestaurantNotificationService restaurantNotificationService;

    @BeforeEach
    void init() {
        restaurantMapper.deleteAll();
    }

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
                .capacity(1)
                .openTime(LocalTime.now().toString())
                .lastOrderTime(LocalTime.now().toString())
                .address("address")
                .detailAddress("detailAddress")
                .build();
        restaurantMapper.save(dto);
        return dto.getRestaurantId();
    }
}