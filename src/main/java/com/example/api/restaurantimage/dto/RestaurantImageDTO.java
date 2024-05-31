package com.example.api.restaurantimage.dto;

import com.example.api.restaurant.dto.RestaurantImageType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantImageDTO {
    private String path;
    private RestaurantImageType restaurantImageType;

    @Builder
    public RestaurantImageDTO(String path, RestaurantImageType restaurantImageType) {
        this.path = path;
        this.restaurantImageType = restaurantImageType;
    }
}