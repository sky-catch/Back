package com.example.api.restaurantnotification;

import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantNotificationDTO;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantNotificationService {

    private final RestaurantNotificationMapper restaurantNotificationMapper;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public long createRestaurantNotification(RestaurantNotificationDTO dto) {
        restaurantMapper.findById(dto.getRestaurantId())
                .orElseThrow(() -> new SystemException("존재하지 않는 식당입니다."));

        restaurantNotificationMapper.save(dto);

        return dto.getNotificationId();
    }
}
