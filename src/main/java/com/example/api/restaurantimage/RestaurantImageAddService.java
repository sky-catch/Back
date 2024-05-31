package com.example.api.restaurantimage;

import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_OWNER;

import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurantimage.dto.AddRestaurantImagesDTO;
import com.example.api.restaurantimage.dto.RestaurantImageDTO;
import com.example.core.exception.SystemException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantImageAddService {

    private final RestaurantImageMapper restaurantImageMapper;
    private final RestaurantService restaurantService;

    @Transactional
    public void addRestaurantImages(AddRestaurantImagesDTO dto, long ownerId,
                                    List<RestaurantImageDTO> restaurantImageDTOS) {
        long restaurantId = dto.getRestaurantId();
        RestaurantDTO findRestaurant = restaurantService.getRestaurantById(restaurantId);
        if (!findRestaurant.isOwner(ownerId)) {
            throw new SystemException(NOT_OWNER.getMessage());
        }
        restaurantImageMapper.deleteRestaurantImages(restaurantId);
        restaurantImageMapper.addRestaurantImages(restaurantId, restaurantImageDTOS);
    }
}
