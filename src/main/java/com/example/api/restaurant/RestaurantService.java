package com.example.api.restaurant;

import com.example.api.restaurant.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    public long createRestaurant(RestaurantDTO dto) {
        // todo 중복 생성 검사
        restaurantMapper.save(dto);

        return dto.getRestaurantId();
    }
}