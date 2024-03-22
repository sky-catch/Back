package com.example.api.savedrestaurant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.core.exception.SystemException;
import org.junit.jupiter.api.AfterEach;
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
class SavedRestaurantServiceTest {

    @Autowired
    private SavedRestaurantService savedRestaurantService;
    @Autowired
    private SavedRestaurantMapper savedRestaurantMapper;

    @BeforeEach
    void init() {
        savedRestaurantMapper.deleteAll();
    }

    @AfterEach
    void cleanup() {
        savedRestaurantMapper.deleteAll();
    }

    @Test
    @DisplayName("새로운 식당 저장을 생성하는 테스트")
    void test1() {
        // given
        CreateSavedRestaurantDTO dto = CreateSavedRestaurantDTO.builder()
                .memberId(1L)
                .restaurantId(1L)
                .build();
        long before = savedRestaurantMapper.findAll().size();

        // when
        savedRestaurantService.createSavedRestaurant(dto);

        // then
        long after = savedRestaurantMapper.findAll().size();
        assertEquals(before + 1, after);
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
}