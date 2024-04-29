package com.example.api.restaurant.dto.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSummaryDTO{
    @Schema(description = "식당 Id", example = "1")
    private long restaurantId;
    @Schema(description = "이미지 url", example = "http://...")
    private String imageUrl;
    @Schema(description = "식당 이름", example = "서울가야밀면")
    private String name;
    @Schema(description = "식당 주소", example = "서울특별시 성동구 서초4길 22-18 반지하층")
    private String fullAddress;
}