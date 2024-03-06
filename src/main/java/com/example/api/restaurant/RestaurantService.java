package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.GetRestaurantRes;
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
        if (restaurantMapper.isAlreadyCreated(dto.getOwnerId())) {
            throw new SystemException("식당은 한 개만 생성할 수 있습니다.");
        }

        restaurantMapper.save(dto);

        return dto.getRestaurantId();
    }

    @Transactional(readOnly = true)
    public RestaurantDTO getRestaurantById(long restaurantId) {
        return restaurantMapper.findById(restaurantId)
                .orElseThrow(() -> new SystemException("존재하지 않는 식당입니다."));
    }

    @Transactional(readOnly = true)
    public GetRestaurantRes getRestaurantInfoById(long restaurantId) {
        GetRestaurantRes getRestaurantRes = restaurantMapper.findRestaurantInfoById(restaurantId)
                .orElseThrow(() -> new SystemException("존재하지 않는 식당입니다."));
//        getRestaurantRes.sortImages();

        return getRestaurantRes;
    }

    public void isOwner(RestaurantDTO restaurant, Owner owner) {
        if (restaurant.getOwnerId() != owner.getOwnerId()) {
            throw new SystemException("식당 주인이 아닙니다.");
        }
    }
}