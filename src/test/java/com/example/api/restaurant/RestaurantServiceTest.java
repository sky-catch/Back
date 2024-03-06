package com.example.api.restaurant;

import static com.example.api.restaurant.dto.RestaurantImageType.NORMAL;
import static com.example.api.restaurant.dto.RestaurantImageType.REPRESENTATIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.restaurant.dto.GetRestaurantImageRes;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantNotificationDTO;
import com.example.api.restaurantimage.RestaurantImageMapper;
import com.example.api.restaurantimage.dto.AddRestaurantImageWithTypeDTO;
import com.example.api.restaurantnotification.RestaurantNotificationMapper;
import com.example.api.restaurantnotification.dto.GetRestaurantNotificationRes;
import com.example.core.exception.SystemException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantMapper restaurantMapper;
    @Autowired
    private RestaurantImageMapper restaurantImageMapper;
    @Autowired
    private RestaurantNotificationMapper restaurantNotificationMapper;

    private RestaurantDTO testRestaurant;

    @BeforeEach
    void init() {
        testRestaurant = RestaurantDTO.builder()
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
    }

    @Test
    @DisplayName("새로운 식당을 생성하는 테스트")
    void test1() {
        // given
        restaurantService.createRestaurant(testRestaurant);

        // when
        long before = restaurantMapper.findAll().size();
        restaurantMapper.save(testRestaurant);
        long actual = restaurantMapper.findAll().size();

        // then
        assertEquals(before + 1, actual);
    }

    @Test
    @DisplayName("식당 상세 정보를 조회하는 테스트")
    void test2() {
        // given
        long createdRestaurantId = restaurantService.createRestaurant(testRestaurant);

        // when
        GetRestaurantRes actual = restaurantService.getRestaurantInfoById(createdRestaurantId);

        // then
        assertThat(actual)
                .extracting("restaurantId", "name", "category")
                .contains(createdRestaurantId, testRestaurant.getName(), testRestaurant.getCategory());
    }

    @Test
    @DisplayName("존재하지 않는 식당 상세 정보를 조회하는 테스트")
    void test3() {
        // given
        long notExistsRestaurantId = 1000L;

        // expected
        assertThatThrownBy(() -> restaurantService.getRestaurantInfoById(notExistsRestaurantId))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("존재하지 않는 식당입니다.");
    }

    // 이미지 정렬 조건
    // 1. REPRESENTATIVE -> NORMAL
    // 2. 생성일 오름차순
    @Test
    @DisplayName("식당 상세 정보 조회 시 식당 이미지들이 정렬되는지 테스트")
    void test4() {
        // given
        long createdRestaurantId = restaurantService.createRestaurant(testRestaurant);

        int expectedImageSize = getCreatedTestImageSize(createdRestaurantId);

        // when
        GetRestaurantRes restaurantInfoById = restaurantService.getRestaurantInfoById(createdRestaurantId);
        List<GetRestaurantImageRes> actual = restaurantInfoById.getImages();

        // then
        assertThat(actual)
                .hasSize(expectedImageSize)
                .extracting("path", "type")
                .containsExactly(
                        tuple("test2", REPRESENTATIVE),
                        tuple("test1", NORMAL),
                        tuple("test3", NORMAL)
                );
    }


    // 공지는 시작일순으로 내림차순 정렬
    @Test
    @DisplayName("식당 상세 정보 조회 시 식당 공지사항들이 정렬되는지 테스트")
    void test5() {
        // given
        long createdRestaurantId = restaurantService.createRestaurant(testRestaurant);

        int expectedNotificationSize = getCreatedTestNotificationSize(createdRestaurantId);

        // when
        GetRestaurantRes restaurantInfoById = restaurantService.getRestaurantInfoById(createdRestaurantId);
        List<GetRestaurantNotificationRes> actual = restaurantInfoById.getNotifications();

        // then
        assertThat(actual)
                .hasSize(expectedNotificationSize)
                .extracting("title", "startDate")
                .containsExactly(
                        tuple("제목입니다.2", LocalDate.of(2024, 2, 1)),
                        tuple("제목입니다.1", LocalDate.of(2024, 1, 1))
                );
    }

    private int getCreatedTestImageSize(long restaurantId) {
        List<AddRestaurantImageWithTypeDTO> restaurantImageWithTypeDTOS = new ArrayList<>();
        restaurantImageWithTypeDTOS.add(AddRestaurantImageWithTypeDTO.builder()
                .path("test1")
                .restaurantImageType(NORMAL)
                .build());
        restaurantImageWithTypeDTOS.add(AddRestaurantImageWithTypeDTO.builder()
                .path("test2")
                .restaurantImageType(REPRESENTATIVE)
                .build());
        restaurantImageWithTypeDTOS.add(AddRestaurantImageWithTypeDTO.builder()
                .path("test3")
                .restaurantImageType(NORMAL)
                .build());
        restaurantImageMapper.addRestaurantImages(restaurantId, restaurantImageWithTypeDTOS);

        return restaurantImageWithTypeDTOS.size();
    }

    private int getCreatedTestNotificationSize(long createdRestaurantId) {
        List<RestaurantNotificationDTO> result = new ArrayList<>();
        RestaurantNotificationDTO dto2 = RestaurantNotificationDTO.builder()
                .restaurantId(createdRestaurantId)
                .ownerId(1L)
                .title("제목입니다." + 2)
                .content("내용입니다." + 2)
                .startDate(LocalDate.of(2024, 2, 1))
                .endDate(LocalDate.of(2024, 2, 1))
                .build();
        result.add(dto2);
        restaurantNotificationMapper.save(dto2);

        RestaurantNotificationDTO dto = RestaurantNotificationDTO.builder()
                .restaurantId(createdRestaurantId)
                .ownerId(1L)
                .title("제목입니다." + 1)
                .content("내용입니다." + 1)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .build();
        result.add(dto);
        restaurantNotificationMapper.save(dto);

        return result.size();
    }
}