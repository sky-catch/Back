package com.example.api.restaurant.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSummaryDTO{
    private long restaurantId;
    private String imageUrl;
    private String name;
    private String fullAddress;
}