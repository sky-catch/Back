package com.example.api.restaurant.dto.enums;

import com.example.core.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

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

    @JsonCreator
    public static Category parsing(String inputValue){
        Optional<Category> result = Stream.of(Category.values())
                .filter(category -> category.koreanName.equals(inputValue))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new SystemException("유효하지 않은 category입니다.");
        }
    }

    public static Category searchCategory(String keyword) {
        return Arrays.stream(Category.values()).filter(category -> category.koreanName.contains(keyword)).findFirst().orElse(null);
    }
}