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

    private GetSearchFilter searchFilter;
    @Schema(description = "레스토랑 수",example = "2453")
    private int restaurantCount;
    @Schema(description = "레스토랑 결과 리스트")
    private List<GetRestaurantSearchListRes> getRestaurantSearchListRes;

    public GetRestaurantSearchRes(SearchFilter searchFilter, int restaurantCount, List<GetRestaurantSearchListRes> getRestaurantSearchListRes) {
        this.searchFilter = new GetSearchFilter(searchFilter);
        this.restaurantCount = restaurantCount;
        this.getRestaurantSearchListRes = getRestaurantSearchListRes;
    }

}
