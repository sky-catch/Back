package com.example.api.restaurant.dto;

import com.example.api.facility.dto.Facility;
import com.example.api.holiday.Days;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "식당 생성 요청값")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantReq {

    @NotBlank
    @Schema(description = "이름", example = "스시미루")
    private String name;
    @NotBlank
    @Schema(description = "카테고리", example = "스시오마카세")
    private String category;
    @Schema(description = "설명", example = "아름다운 맛과 다채로운 구성, 술 곁들이기 아늑한 분위기의 스시오마카세")
    private String content;
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{4}-\\d{4}$")
    @Schema(description = "전화번호", example = "02-6402-4044")
    private String phone;
    @NotNull
    @Min(1)
    @Schema(description = "예약 최대 인원 수", example = "2")
    private int tablePersonMax;
    @NotNull
    @Min(1)
    @Schema(description = "예약 최소 인원 수", example = "4")
    private int tablePersonMin;
    @NotNull
    @Schema(description = "오픈시간", example = "11:00:00")
    private LocalTime openTime;
    @NotNull
    @Schema(description = "주문마감시간", example = "20:20:00")
    private LocalTime lastOrderTime;
    @NotNull
    @Schema(description = "마감시간", example = "22:00:00")
    private LocalTime closeTime;
    @NotBlank
    @Schema(description = "주소", example = "압구정로데오")
    private String address;
    @NotBlank
    @Schema(description = "상세주소", example = "서울특별시 강남구 언주로170길 26-6 2층")
    private String detailAddress;
    @Schema(description = "점심가격", example = "70000")
    private int lunchPrice;
    @Schema(description = "저녁가격", example = "140000")
    private int dinnerPrice;
    @Schema(description = "휴무일", example = "월,화")
    private Days days;
    @Schema(description = "[(PARKING, 주차 가능), (VALET_PARKING, 발렛 가능), (CORKAGE, 콜키지 가능), (CORKAGE_FREE, 콜키지 프리)," +
            " (RENT, 대관 가능), (NO_KIDS, 노키즈존), (WINE_DELIVERY, 와인배송), (LETTERING, 레터링), (SOMMELIER, 전문 소믈리에)," +
            " (PET, 반려동물 동반), (ACCESSIBLE, 장애인 편의시설)]", example = "[\"PARKING\", \"CORKAGE\"]")
    private List<Facility> facilities;

    @Schema(hidden = true)
    private long ownerId;
}
