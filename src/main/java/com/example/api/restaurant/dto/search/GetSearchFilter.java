package com.example.api.restaurant.dto.search;

import com.example.api.restaurant.dto.enums.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetSearchFilter{
    @Schema(description = "날짜", example = "2024-01-01")
    private String date;
    @Schema(description = "30분 단위",example = "17:30")
    private String time;
    @Schema(description = "사람 수",example = "2")
    private int personCount;
    @Schema(description = "지역. 서울, 경기, 인천, 부산, 제주, 울산, 경남, 대구, 경북, 강원, 대전, 충남, 충북, 세종, 전남, 전북, 광주", example = "서울")
    private String koreanCity;
    @Schema(description = "핫플레이스", example = "방배")
    private String hotPlace;
    @Schema(description = "카테고리. 스시오마카세, 한우오마카세, 스테이크, 한식, 소고기구이, 중식, 일식, 이탈리아음식, 프랑스음식, 아시아음식, 와인, 맥주, 기타", example = "스시오마카세")
    private String category;
    @Schema(description = "최소가격. 미지정시 0", example = "100000")
    private int minPrice;
    @Schema(description = "최대가격. 미지정시 0", example = "200000")
    private int maxPrice;
    @Schema(description = "정렬 순서. 기본순, 별점순, 가격낮은순, 가격높은순", example = "별점순")
    private String orderType;

    public GetSearchFilter(SearchFilter searchFilter) {
        this.date = searchFilter.getDate();
        this.time = searchFilter.getTime();
        this.personCount = searchFilter.getPersonCount();
        this.koreanCity = (searchFilter.getKoreanCity() == null) ? null : searchFilter.getKoreanCity().getKoreanName();
        this.hotPlace = (searchFilter.getHotPlace() == null) ? null : searchFilter.getHotPlace().getKoreanName();
        this.category = (searchFilter.getCategory() == null) ? null : searchFilter.getCategory().getKoreanName();
        this.minPrice = searchFilter.getMinPrice();
        this.maxPrice = searchFilter.getMaxPrice();
        this.orderType = (searchFilter.getOrderType() == null) ? "기본순" : searchFilter.getOrderType().getKoreanName();
    }
}