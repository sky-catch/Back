package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    @Transactional
    public long createRestaurant(RestaurantDTO dto) {
        // todo 중복 생성 검사
        restaurantMapper.save(dto);

        return dto.getRestaurantId();
    }

    @Transactional(readOnly = true)
    public RestaurantDTO getRestaurantById(long restaurantId) {
        return restaurantMapper.findById(restaurantId)
                .orElseThrow(() -> new SystemException("존재하지 않는 식당입니다."));
    }

    public void isOwner(RestaurantDTO restaurant, Owner owner) {
        if (restaurant.getOwnerId() != owner.getOwnerId()) {
            throw new SystemException("식당 주인이 아닙니다.");
        }
    }
}