package com.example.api.restaurant.dto;

import com.example.api.facility.dto.GetFacilityRes;
import com.example.api.restaurantnotification.dto.GetRestaurantNotificationRes;
import com.example.api.review.dto.GetReviewCommentRes;
import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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
public class GetRestaurantWithReview extends BaseDTO {

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
    @Schema(description = "주소", example = "압구정로데오")
    private String address;
    @Schema(description = "상세주소", example = "서울특별시 강남구 언주로170길 26-6 2층")
    private String detailAddress;
    @Schema(description = "점심가격", example = "70000")
    private int lunchPrice;
    @Schema(description = "저녁가격", example = "140000")
    private int dinnerPrice;
    @Schema(description = "식당 저장 수", example = "1")
    private long savedCount;
    @Schema(description = "식당 리뷰 수", example = "1")
    private long reviewCount;
    @Schema(description = "식당 리뷰 평균", example = "5")
    private float reviewAvg;
    @Schema(description = "식당 이미지들")
    private List<GetRestaurantImageRes> images;
    @Schema(description = "식당 공지사항들")
    private List<GetRestaurantNotificationRes> notifications;
    @Schema(description = "식당 부대사항들")
    private List<GetFacilityRes> facilities;
    @Schema(description = "식당 리뷰들")
    private List<GetReviewCommentRes> reviewComments;


    public GetRestaurantWithReview(GetRestaurantRes getRestaurantRes, List<GetReviewCommentRes> reviewComments) {
        super(getRestaurantRes.getCreatedDate(), getRestaurantRes.getUpdatedDate());
        this.restaurantId = getRestaurantRes.getRestaurantId();
        this.ownerId = getRestaurantRes.getOwnerId();
        this.name = getRestaurantRes.getName();
        this.category = getRestaurantRes.getCategory();
        this.content = getRestaurantRes.getContent();
        this.phone = getRestaurantRes.getPhone();
        this.tablePersonMax = getRestaurantRes.getTablePersonMax();
        this.tablePersonMin = getRestaurantRes.getTablePersonMin();
        this.openTime = getRestaurantRes.getOpenTime();
        this.lastOrderTime = getRestaurantRes.getLastOrderTime();
        this.closeTime = getRestaurantRes.getCloseTime();
        this.address = getRestaurantRes.getAddress();
        this.detailAddress = getRestaurantRes.getDetailAddress();
        this.lunchPrice = getRestaurantRes.getLunchPrice();
        this.dinnerPrice = getRestaurantRes.getDinnerPrice();
        this.savedCount = getRestaurantRes.getSavedCount();
        this.reviewCount = getRestaurantRes.getReviewCount();
        this.reviewAvg = getRestaurantRes.getReviewAvg();
        this.images = getRestaurantRes.getImages();
        this.notifications = getRestaurantRes.getNotifications();
        this.facilities = getRestaurantRes.getFacilities();
        this.reviewComments = reviewComments;
    }
}
