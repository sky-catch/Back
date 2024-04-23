package com.example.api.restaurant.dto.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum HotPlace {
    GANGNAM_YEOKSAM_SEONLEUNG("강남/역삼/선릉"),
    GANGNAM_DISTRICT("강남구청"),
    GEONDAE_GUNJA_GUUI("건대/군자/구의"),
    GEUMHO_OKSU_SINDANG("금호/옥수/신당"),
    MYEONGDONG_EULJIRO_CHUNGMURO("명동/을지로/충무로"),
    BANG_I("방이"),
    BUKCHON_SAMCHEONG("북촌/삼청"),
    SAMSEONG_DAECHI("삼성/대치"),
    SANGSU_HAPJEONG_MANGWON("상수/합정/망원"),
    SEOUL_STATION_HOEHYEON("서울역/회현"),
    SEOCHO_BANBAE("서초/반배"),
    SEOCHON("서촌"),
    SEONGSU_SEOULLIB("성수/서울숲"),
    SINSA_NONHYEON("신사/논현"),
    SINCHON_HONGDAE_SEOGYO("신촌/홍대/서교"),
    APGUJEONG_CHEONGDAM("압구정/청담"),
    YANGJE_DOGOK("양재/도곡"),
    YEONNAM("연남"),
    YEONGDEUNGPO_YEOUIDO("영등포/여의도"),
    YONGSAN_SAMGAKJI("용산/삼각지"),
    ITAEWON_HANNAM("이태원/한남"),
    JAMSIL_SONGPA("잠실/송파"),
    JONGNO_GWANGHWA_GUN("종로/광화군"),
    SEONGNAM_BUNDANG("성남/분당"),
    SUWON_GWANGYO("수원/광교"),
    PANGYO("판교");

    private final String koreanName;

    HotPlace(String koreanName) {
        this.koreanName = koreanName;
    }

    public static String getHotPlaceValue(String hotPlace) {
        return Arrays.stream(HotPlace.values())
                .map(HotPlace::getKoreanName)
                .filter(name -> Arrays.stream(name.split("/")).anyMatch(hotPlace::contains))
                .findFirst()
                .orElse(null);
    }

}