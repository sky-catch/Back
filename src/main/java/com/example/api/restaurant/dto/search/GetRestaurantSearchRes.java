package com.example.api.restaurant.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantSearchRes {

    private SearchFilter searchFilter;
    @Schema(description = "레스토랑 수",example = "2453")
    private int restaurantCount;
    private List<GetRestaurantSearchListRes> getRestaurantSearchListRes;
}
