package com.example.api.savedrestaurant;

import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.exception.RestaurantExceptionType;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedRestaurantService {

    private final SavedRestaurantMapper savedRestaurantMapper;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public void createSavedRestaurant(CreateSavedRestaurantDTO dto) {
        if (savedRestaurantMapper.isAlreadyExistsByRestaurantIdAndMemberId(dto.getRestaurantId(), dto.getMemberId())) {
            throw new SystemException("해당 식당은 이미 저장하였습니다.");
        }
        RestaurantDTO restaurant = restaurantMapper.findById(dto.getRestaurantId())
                .orElseThrow(() -> new SystemException(RestaurantExceptionType.NOT_FOUND));
        restaurant.increaseSavedCount();
        restaurantMapper.increaseSavedCount(restaurant);

        SavedRestaurantDTO savedRestaurant = dto.toSavedRestaurantDTO();
        savedRestaurantMapper.save(savedRestaurant);
    }

    @Transactional
    public void deleteSavedRestaurant(DeleteSavedRestaurantDTO dto) {
        RestaurantDTO restaurant = restaurantMapper.findById(dto.getRestaurantId())
                .orElseThrow(() -> new SystemException(RestaurantExceptionType.NOT_FOUND));
        restaurant.decreaseSavedCount();
        restaurantMapper.decreaseSavedCount(restaurant);

        SavedRestaurantDTO savedRestaurant = dto.toSavedRestaurantDTO();
        savedRestaurantMapper.delete(savedRestaurant);
    }
}