package com.example.api.restaurant.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantSearchSummaryRes {

    @Schema(description = "지역. 서울, 경기, 인천, 부산, 제주, 울산, 경남, 대구, 경북, 강원, 대전, 충남, 충북, 세종, 전남, 전북, 광주. ", example = "서울")
    private String city;
    @Schema(description = "지역 식당 수", example = "123")
    private int cityRestaurantCount;
    @Schema(description = "핫플레이스.", example = "서울역/회현")
    private String hotPlace;
    @Schema(description = "지역 식당 수", example = "123")
    private int hotPlaceRestaurantCount;
    @Schema(description = "카테고리", example = "양식")
    private String category;
    @Schema(description = "지역 식당 수", example = "123")
    private int categoryRestaurantCount;
    @Schema(description = "식당 요약 정보 리스트.")
    private List<RestaurantSummaryDTO> restaurantSummaryDTOList;

}
