package com.example.api.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRestaurant {

    @Schema(description = "식당 ID", example = "1")
    private long restaurantId;
    @Schema(description = "이미지 url", example = "http://...")
    private String imageUrl;
    @Schema(description = "식당 이름", example = "서울가야밀면")
    private String name;
    @Schema(description = "식당 주소", example = "서울특별시 성동구 서초4길 22-18 반지하층")
    private String fullAddress;
    @Schema(description = "주소", example = "서울")
    private String address;
    @Schema(description = "카테고리", example = "스시오마카세")
    private String category;
    @Schema(description = "식당 리뷰 평균", example = "5")
    private float reviewAvg;
    @Schema(description = "이 식당을 저장했는지, 저장했으면 true", example = "true")
    private boolean savedRestaurant;


}
