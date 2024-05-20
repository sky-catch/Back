package com.example.api.restaurant.dto;

import com.example.api.facility.dto.GetFacilityRes;
import com.example.api.holiday.Day;
import com.example.api.holiday.HolidayDTO;
import com.example.api.holiday.Holidays;
import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.api.restaurantnotification.dto.GetRestaurantNotificationRes;
import com.example.api.review.dto.GetReviewCommentRes;
import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "식당 이름으로 식당 조회 응답값")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantInfo extends BaseDTO {

    @Schema(description = "식당 ID", example = "1")
    private long restaurantId;
    @Schema(description = "사장 ID", example = "1")
    private long ownerId;
    @Schema(description = "이름", example = "스시미루")
    private String name;
    @Schema(description = "카테고리", example = "스시오마카세")
    private String category;
    @Schema(description = "설명", example = "아름다운 맛과 다채로운 구성, 술 곁들이기 아늑한 분위기의 스시오마카세")
    private String content;
    @Schema(description = "전화번호", example = "02-6402-4044")
    private String phone;
    @Schema(description = "예약 최대 인원 수", example = "2")
    private int tablePersonMax;
    @Schema(description = "예약 최소 인원 수", example = "4")
    private int tablePersonMin;
    @Schema(description = "오픈시간", example = "11:00:00", type = "string")
    private String openTime;
    @Schema(description = "주문마감시간", example = "20:20:00", type = "string")
    private String lastOrderTime;
    @Schema(description = "마감시간", example = "22:00:00", type = "string")
    private String closeTime;
    @Schema(description = "주소", example = "서울")
    private String address;
    @Schema(description = "상세주소", example = "서울특별시 강남구 언주로170길 26-6 2층")
    private String detailAddress;
    @Schema(description = "위도", example = "33.450701")
    private BigDecimal lat;
    @Schema(description = "경도", example = "126.570667")
    private BigDecimal lng;
    @Schema(description = "점심가격", example = "70000")
    private int lunchPrice;
    @Schema(description = "저녁가격", example = "140000")
    private int dinnerPrice;
    @Schema(description = "휴무일", example = "{\"holidays\": [\"MONDAY\", \"TUESDAY\"]}")
    private Holidays holiDays;
    @Schema(description = "예약 가능 시작 날짜", example = "2024-03-01", type = "string")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reservationBeginDate;
    @Schema(description = "예약 가능 종료 날짜", example = "2024-04-01")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reservationEndDate;
    @Schema(description = "식당 저장 수", example = "1")
    private long savedCount;
    @Schema(description = "식당 리뷰 수", example = "1")
    private long reviewCount;
    @Schema(description = "식당 리뷰 평균", example = "5")
    private float reviewAvg;
    @Schema(description = "로그인 회원 식당 저장 여부", example = "true")
    private boolean isSaved;
    @Schema(description = "식당 이미지들")
    private List<GetRestaurantImageRes> images;
    @Schema(description = "식당 공지사항들")
    private List<GetRestaurantNotificationRes> notifications;
    @Schema(description = "식당 부대사항들")
    private List<GetFacilityRes> facilities;
    @Schema(description = "식당 리뷰들")
    private List<GetReviewCommentRes> reviewComments;


    public GetRestaurantInfo(GetRestaurantInfoRes getRestaurantInfoRes, List<GetReviewCommentRes> reviewComments,
                             ReservationAvailableDateDTO reservationAvailableDateDTO, List<HolidayDTO> holidayDTOS) {

        super(getRestaurantInfoRes.getCreatedDate(), getRestaurantInfoRes.getUpdatedDate());
        this.restaurantId = getRestaurantInfoRes.getRestaurantId();
        this.ownerId = getRestaurantInfoRes.getOwnerId();
        this.name = getRestaurantInfoRes.getName();
        this.category = getRestaurantInfoRes.getCategory();
        this.content = getRestaurantInfoRes.getContent();
        this.phone = getRestaurantInfoRes.getPhone();
        this.tablePersonMax = getRestaurantInfoRes.getTablePersonMax();
        this.tablePersonMin = getRestaurantInfoRes.getTablePersonMin();
        this.openTime = getRestaurantInfoRes.getOpenTime();
        this.lastOrderTime = getRestaurantInfoRes.getLastOrderTime();
        this.closeTime = getRestaurantInfoRes.getCloseTime();
        this.address = getRestaurantInfoRes.getAddress();
        this.detailAddress = getRestaurantInfoRes.getDetailAddress();
        this.lat = getRestaurantInfoRes.getLat();
        this.lng = getRestaurantInfoRes.getLng();
        this.lunchPrice = getRestaurantInfoRes.getLunchPrice();
        this.dinnerPrice = getRestaurantInfoRes.getDinnerPrice();
        this.holiDays = getHolidays(holidayDTOS);
        this.reservationBeginDate = reservationAvailableDateDTO.getBeginDate();
        this.reservationEndDate = reservationAvailableDateDTO.getEndDate();
        this.savedCount = getRestaurantInfoRes.getSavedCount();
        this.reviewCount = getRestaurantInfoRes.getReviewCount();
        this.reviewAvg = getRestaurantInfoRes.getReviewAvg();
        this.images = getRestaurantInfoRes.getImages();
        this.notifications = getRestaurantInfoRes.getNotifications();
        this.facilities = getRestaurantInfoRes.getFacilities();
        this.reviewComments = reviewComments;
        this.isSaved = getRestaurantInfoRes.isSaved();
    }

    private Holidays getHolidays(List<HolidayDTO> holidayDTOS) {
        List<Day> days = holidayDTOS.stream()
                .map(HolidayDTO::getDay)
                .collect(Collectors.toList());
        return new Holidays(days);
    }
}
