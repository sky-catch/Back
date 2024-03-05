package com.example.api.restaurant;

import static com.example.api.restaurant.dto.RestaurantImageType.NORMAL;
import static com.example.api.restaurant.dto.RestaurantImageType.REPRESENTATIVE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurantimage.RestaurantImageMapper;
import com.example.api.restaurantimage.dto.AddRestaurantImageWithTypeDTO;
import com.example.core.exception.SystemException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private RestaurantMapper restaurantMapper;
    @Autowired
    private RestaurantImageMapper restaurantImageMapper;

    @Test
    @DisplayName("새 가게를 생성하는 테스트")
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
        long before = restaurantMapper.findAll().size();
        restaurantService.createRestaurant(dto);
        long actual = restaurantMapper.findAll().size();

        // then
        assertEquals(before + 1, actual);
    }

    @Test
    @DisplayName("가게 상세 정보를 조회하는 테스트")
    void test2() {
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
        long createdRestaurantId = restaurantService.createRestaurant(dto);

        List<AddRestaurantImageWithTypeDTO> restaurantImageWithTypeDTOS = new ArrayList<>();
        restaurantImageWithTypeDTOS.add(AddRestaurantImageWithTypeDTO.builder()
                .path("test1")
                .restaurantImageType(REPRESENTATIVE)
                .build());
        restaurantImageWithTypeDTOS.add(AddRestaurantImageWithTypeDTO.builder()
                .path("test2")
                .restaurantImageType(NORMAL)
                .build());
        restaurantImageMapper.addRestaurantImages(createdRestaurantId, restaurantImageWithTypeDTOS);

        // when
        GetRestaurantRes actual = restaurantService.getRestaurantInfoById(createdRestaurantId);

        // then
        assertEquals(dto.getName(), actual.getName());
        assertEquals(restaurantImageWithTypeDTOS.size(), actual.getImages().size());
    }

    @Test
    @DisplayName("존재하지 않는 가게 상세 정보를 조회하는 테스트")
    void test3() {
        // given
        long notExistsRestaurantId = 1000L;

        // expected
        assertThatThrownBy(() -> restaurantService.getRestaurantInfoById(notExistsRestaurantId))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("존재하지 않는 식당입니다.");
    }
}