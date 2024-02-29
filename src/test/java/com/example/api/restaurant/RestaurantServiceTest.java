package com.example.api.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.restaurant.dto.RestaurantDTO;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:truncate.sql")
class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    @DisplayName("새 가게를 생성하면 생성된 가게 번호를 반환하는 테스트")
    void test1() {
        // given
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
        restaurantService.createRestaurant(dto);

        // when
        long expected = restaurantService.createRestaurant(dto);

        // then
        assertEquals(expected, 2);
    }
}