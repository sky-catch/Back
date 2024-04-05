package com.example.api.savedrestaurant;

import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_VALID_SAVED_COUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import java.time.LocalTime;
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
class SavedRestaurantServiceTest {

    private final SavedRestaurantService savedRestaurantService;
    private final SavedRestaurantMapper savedRestaurantMapper;
    private final RestaurantMapper restaurantMapper;

    private RestaurantDTO testRestaurant;

    @BeforeEach
    void init() {
        savedRestaurantMapper.deleteAll();

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

    @AfterEach
    void cleanup() {
        savedRestaurantMapper.deleteAll();
        restaurantMapper.deleteAll();
    }

    @Test
    @DisplayName("새로운 식당 저장을 생성하는 테스트")
    void test1() {
        // given
        CreateSavedRestaurantDTO dto = CreateSavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(testRestaurant.getRestaurantId())
                .build();
        long before = savedRestaurantMapper.findAll().size();

        // when
        savedRestaurantService.createSavedRestaurant(dto);

        // then
        long after = savedRestaurantMapper.findAll().size();
        RestaurantDTO actual = restaurantMapper.findById(testRestaurant.getRestaurantId()).get();
        assertAll(() -> {
            assertEquals(before + 1, after);
            assertEquals(testRestaurant.getSavedCount() + 1, actual.getSavedCount());
        });
    }

    @Test
    @DisplayName("기존 식당 저장이 존재할 때 식당 저장을 생성하면 예외가 발생하는 테스트")
    void test2() {
        // given
        CreateSavedRestaurantDTO dto = CreateSavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(1L)
                .build();
        savedRestaurantService.createSavedRestaurant(dto);

        // expected
        assertThatThrownBy(() -> savedRestaurantService.createSavedRestaurant(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("해당 식당은 이미 저장하였습니다.");
    }

    @Test
    @DisplayName("식당 저장을 삭제하는 테스트")
    void test3() {
        // given
        long before = savedRestaurantMapper.findAll().size();

        SavedRestaurantDTO savedRestaurant = SavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(testRestaurant.getRestaurantId())
                .build();
        savedRestaurantMapper.save(savedRestaurant);
        testRestaurant.increaseSavedCount();
        restaurantMapper.increaseSavedCount(testRestaurant);

        DeleteSavedRestaurantDTO dto = DeleteSavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(testRestaurant.getRestaurantId())
                .build();

        // when
        savedRestaurantService.deleteSavedRestaurant(dto);

        // then
        long after = savedRestaurantMapper.findAll().size();
        RestaurantDTO actual = restaurantMapper.findById(testRestaurant.getRestaurantId()).get();
        assertAll(() -> {
            assertEquals(before, after);
            assertEquals(testRestaurant.getSavedCount() - 1, actual.getSavedCount());
        });
    }

    @Test
    @DisplayName("식당 저장 값이 0보다 작거나 같을 때 식당 저장을 하면 예외가 발생하는 테스트")
    void deleteSavedRestaurantWithSavedCountLessOrEqualThan0Test() {
        // given
        long before = savedRestaurantMapper.findAll().size();

        SavedRestaurantDTO savedRestaurant = SavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(testRestaurant.getRestaurantId())
                .build();
        savedRestaurantMapper.save(savedRestaurant);

        DeleteSavedRestaurantDTO dto = DeleteSavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(testRestaurant.getRestaurantId())
                .build();

        // expected
        assertThatThrownBy(() -> savedRestaurantService.deleteSavedRestaurant(dto))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining(NOT_VALID_SAVED_COUNT.getMessage());
    }
}