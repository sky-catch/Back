package com.example.api.restaurant.dto;

import com.example.api.holiday.Days;
import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "식당 생성 요청값")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantReq extends BaseDTO {

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
    @Schema(description = "점심가격", example = "7만원")
    private int lunchPrice;
    @Schema(description = "저녁가격", example = "14만원")
    private int dinnerPrice;

    @Schema(description = "휴무일", example = "월,화")
    private Days days;

    private long savedCount;
    private long reviewCount;
    private float reviewAvg;
}
