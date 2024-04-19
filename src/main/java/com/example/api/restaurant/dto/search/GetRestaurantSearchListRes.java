package com.example.api.restaurant.dto.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantSearchListRes {

    @Schema(description = "식당 ID", example = "1")
    private long restaurantId;
    @Schema(description = "식당 이미지 url")
    private String imageUrl;
    @Schema(description = "이름", example = "스시미루")
    private String name;
    @Schema(description = "식당 설명. 30글자 제한", example = "아름다운 맛과 다채로운 구성, 술 곁들이기 아늑한 분위기의 ...")
    private String content;
    @Schema(description = "식당 리뷰 평균", example = "5")
    private float reviewAvg;
    @Schema(description = "식당 리뷰 수", example = "1")
    private long reviewCount;
    @Schema(description = "카테고리", example = "스시오마카세")
    private String category;
    @Schema(description = "위치", example = "서울")
    private String koreanCity;
    @Schema(description = "점심가격", example = "70000")
    private int lunchPrice;
    @Schema(description = "저녁가격", example = "140000")
    private int dinnerPrice;
    @Schema(description = "저장한 식당인지. 저장했으면 true", example = "true")
    private boolean savedRestaurant;
    @Schema(description = "예약 가능 시간 리스트 형태. 최대 3개", example = "17:30")
    private List<String> possibleReservationTime;
    @JsonIgnore
    private LocalTime lastOrderTime;
    
}
