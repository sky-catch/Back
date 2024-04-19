package com.example.api.restaurant.dto.enums;

import com.example.api.facility.dto.Facility;
import com.example.core.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum KoreanCity {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    BUSAN("부산"),
    JEJU("제주"),
    ULSAN("울산"),
    GYEONGNAM("경남"),
    DAEGU("대구"),
    GYEONGBUK("경북"),
    GANGWON("강원"),
    DAEJEON("대전"),
    CHUNGNAM("충남"),
    CHUNGBUK("충북"),
    SEJONG("세종"),
    JEONNAM("전남"),
    JEONBUK("전북"),
    GWANGJU("광주");

    private final String koreanName;

    KoreanCity(String koreanName) {
        this.koreanName = koreanName;
    }

    @JsonCreator
    public static KoreanCity parsing(String inputValue){
        Optional<KoreanCity> result = Stream.of(KoreanCity.values())
                .filter(koreanCity -> koreanCity.koreanName.equals(inputValue))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new SystemException("유효하지 않은 koreanCity입니다.");
        }
    }

    public String getKoreanName() {
        return koreanName;
    }

    public static KoreanCity searchKoreanCity(String keyword) {
        return Arrays.stream(KoreanCity.values())
                .filter(koreanCity -> koreanCity.koreanName.equals(keyword))
                .findFirst().orElse(null);
    }

}