package com.example.api.restaurant.dto.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    SUSHI_OMAKASE("스시오마카세"),
    HANWOO_OMAKASE("한우오마카세"),
    STEAK("스테이크"),
    KOREAN("한식"),
    BEEF_GRILL("소고기구이"),
    CHINESE("중식"),
    JAPANESE("일식"),
    ITALIAN("이탈리아음식"),
    FRENCH("프랑스음식"),
    ASIAN("아시아음식"),
    WINE("와인"),
    BEER("맥주"),
    OTHER("기타");

    private final String koreanName;

    Category(String koreanName) {
        this.koreanName = koreanName;
    }

    public static Category searchCategory(String keyword) {
        return Arrays.stream(Category.values()).filter(category -> category.koreanName.contains(keyword)).findFirst().orElse(null);
    }
}