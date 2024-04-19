package com.example.api.restaurant.dto.enums;

import com.example.core.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum OrderType {
    DefaultOrder("기본순"),
    RatingOrder("별점순"),
    LowPriceOrder("가격낮은순"),
    HighPriceOrder("가격높은순");

    private final String koreanName;

    OrderType(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonCreator
    public static OrderType parsing(String inputValue){
        Optional<OrderType> result = Stream.of(OrderType.values())
                .filter(orderType -> orderType.koreanName.equals(inputValue))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new SystemException("유효하지 않은 orderType입니다.");
        }
    }

}
