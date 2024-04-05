package com.example.api.restaurant;

import static com.example.api.restaurant.dto.RestaurantImageType.NORMAL;
import static com.example.api.restaurant.dto.RestaurantImageType.REPRESENTATIVE;
import static com.example.api.restaurant.exception.RestaurantExceptionType.CAN_CREATE_ONLY_ONE;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_UNIQUE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.holiday.Days;
import com.example.api.restaurant.dto.CreateRestaurantReq;
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
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
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
class RestaurantServiceTest {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantImageMapper restaurantImageMapper;
    private final RestaurantNotificationMapper restaurantNotificationMapper;

    private RestaurantDTO testRestaurant;

    private CreateRestaurantReq createRestaurantReq;

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
                .openTime(LocalTime.now())
                .lastOrderTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .address("address")
                .detailAddress("detailAddress")
                .build();

        createRestaurantReq = CreateRestaurantReq.builder()
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
                .reservationBeginDate(LocalDate.now())
                .reservationEndDate(LocalDate.now())
                .address("address")
                .detailAddress("detailAddress")
                .days(Days.of(new ArrayList<>()))
                .build();
    }

    @AfterEach
    void cleanup() {
        restaurantMapper.deleteAll();
    }

    @Test
    @DisplayName("새로운 식당을 생성하는 테스트")
    void test1() {
        // given
        RestaurantDTO dto = RestaurantDTO.builder()
                .ownerId(2L)
                .name("name2")
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
        restaurantMapper.save(dto);
        long before = restaurantMapper.findAll().size();

        // when
        restaurantService.createRestaurant(createRestaurantReq);

        // then
        long after = restaurantMapper.findAll().size();
        assertEquals(before + 1, after);
    }

    @Test
    @DisplayName("여러 식당을 생성하면 예외가 발생하는 테스트")
    void test2() {
        // given
        restaurantService.createRestaurant(createRestaurantReq);

        // expected
        assertThatThrownBy(() -> restaurantService.createRestaurant(createRestaurantReq))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(CAN_CREATE_ONLY_ONE.getMessage());
    }

    @Test
    @DisplayName("중복 이름의 식당을 생성하면 예외가 발생하는 테스트")
    void test3() {
        // given
        RestaurantDTO dto = RestaurantDTO.builder()
                .ownerId(2L)
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
        restaurantMapper.save(dto);

        // expected
        assertThatThrownBy(() -> restaurantService.createRestaurant(createRestaurantReq))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(NOT_UNIQUE_NAME.getMessage());
    }

    @Test
    @DisplayName("식당 상세 정보를 조회하는 테스트")
    void test4() {
        // given
        long createdRestaurantId = restaurantService.createRestaurant(createRestaurantReq);

        // when
        GetRestaurantRes actual = restaurantService.getRestaurantInfoById(createdRestaurantId);
        System.out.println("actual = " + actual);

        // then
        assertThat(actual)
                .extracting("restaurantId", "name", "category")
                .contains(createdRestaurantId, testRestaurant.getName(), testRestaurant.getCategory());
    }

    @Test
    @DisplayName("존재하지 않는 식당 상세 정보를 조회하는 테스트")
    void test5() {
        // given
        long notExistsRestaurantId = 1000L;

        // expected
        assertThatThrownBy(() -> restaurantService.getRestaurantInfoById(notExistsRestaurantId))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(NOT_FOUND.getMessage());
    }

    // 이미지 정렬 조건
    // 1. REPRESENTATIVE -> NORMAL
    // 2. 생성일 오름차순
    @Test
    @DisplayName("식당 상세 정보 조회 시 식당 이미지들이 정렬되는지 테스트")
    void test6() {
        // given
        long createdRestaurantId = restaurantService.createRestaurant(createRestaurantReq);

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
    void test7() {
        // given
        long createdRestaurantId = restaurantService.createRestaurant(createRestaurantReq);

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

    @Test
    @DisplayName("식당 생성 시 이미 식당을 만든 경우 예외 발생하는 테스트")
    void test8() {
        // given
        restaurantService.createRestaurant(createRestaurantReq);

        // expected
        assertThatThrownBy(() -> restaurantService.createRestaurant(createRestaurantReq))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(CAN_CREATE_ONLY_ONE.getMessage());
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