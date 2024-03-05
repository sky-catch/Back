package com.example.api.restaurantnotification;

import com.example.api.restaurant.dto.RestaurantNotificationDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestaurantNotificationMapper {

    void save(RestaurantNotificationDTO dto);
}
