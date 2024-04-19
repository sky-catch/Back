package com.example.api.restaurant.dto.search;

import com.example.api.restaurant.dto.enums.Category;
import com.example.api.restaurant.dto.enums.KoreanCity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantSearchSummaryRes {

    private KoreanCity city;
    private int cityRestaurantCount;
    private Category category;
    private int categoryRestaurantCount;
    private List<RestaurantSummaryDTO> restaurantSummaryDTOList;

}
