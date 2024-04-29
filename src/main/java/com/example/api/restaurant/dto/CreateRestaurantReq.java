package com.example.api.restaurant.dto;

import com.example.api.facility.dto.Facility;
import com.example.api.holiday.Days;
import com.example.api.restaurant.dto.enums.Category;
import com.example.api.restaurant.dto.enums.KoreanCity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "식당 생성 요청값")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantReq {

    @NotBlank
    @Schema(description = "이름", example = "스시미루")
    private String name;
    @NotNull
    @Schema(description = "카테고리", example = "스시오마카세")
    private Category category;
    @Schema(description = "설명", example = "아름다운 맛과 다채로운 구성, 술 곁들이기 아늑한 분위기의 스시오마카세")
    private String content;
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{4}-\\d{4}$")
    @Schema(description = "전화번호", example = "02-6402-4044")
    private String phone;
    @NotNull
    @Min(1)
    @Schema(description = "예약 최대 인원 수", example = "10")
    private int tablePersonMax;
    @NotNull
    @Min(1)
    @Schema(description = "예약 최소 인원 수", example = "2")
    private int tablePersonMin;
    @NotNull
    @Schema(name = "openTime", description = "오픈시간", example = "11:00:00", type = "string")
    private LocalTime openTime;
    @NotNull
    @Schema(name = "lastOrderTime", description = "주문마감시간", example = "20:20:00", type = "string")
    private LocalTime lastOrderTime;
    @NotNull
    @Schema(description = "마감시간", example = "22:00:00", type = "string")
    private LocalTime closeTime;
    @NotNull
    @Schema(description = "지역. 서울, 경기, 인천, 부산, 제주, 울산, 경남, 대구, 경북, 강원, 대전, 충남, 충북, 세종, 전남, 전북, 광주", example = "서울")
    private KoreanCity address;
    @NotBlank
    @Schema(description = "상세주소", example = "서울특별시 강남구 언주로170길 26-6 2층")
    private String detailAddress;
    @NotNull
    @Schema(description = "위도", example = "33.450701")
    private BigDecimal lat;
    @NotNull
    @Schema(description = "경도", example = "126.570667")
    private BigDecimal lng;
    @Schema(description = "점심가격", example = "70000")
    private int lunchPrice;
    @Schema(description = "저녁가격", example = "140000")
    private int dinnerPrice;
    @Schema(description = "휴무일", example = "{\"days\": [\"MONDAY\", \"TUESDAY\"]}")
    private Days days;
    @NotNull
    @Schema(description = "예약 가능 시작 날짜", example = "2024-03-01", type = "string")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reservationBeginDate;
    @NotNull
    @Schema(description = "예약 가능 종료 날짜", example = "2024-04-01")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reservationEndDate;
    @Schema(description = "[(PARKING, 주차 가능), (VALET_PARKING, 발렛 가능), (CORKAGE, 콜키지 가능), (CORKAGE_FREE, 콜키지 프리)," +
            " (RENT, 대관 가능), (NO_KIDS, 노키즈존), (WINE_DELIVERY, 와인배송), (LETTERING, 레터링), (SOMMELIER, 전문 소믈리에)," +
            " (PET, 반려동물 동반), (ACCESSIBLE, 장애인 편의시설)]", example = "[\"PARKING\", \"CORKAGE\"]")
    private List<Facility> facilities;

    @Schema(hidden = true)
    private long ownerId;
}
