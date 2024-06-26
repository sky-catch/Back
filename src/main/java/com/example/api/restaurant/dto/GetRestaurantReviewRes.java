package com.example.api.restaurant.dto;

import com.example.api.facility.dto.GetFacilityRes;
import com.example.api.restaurantnotification.dto.GetRestaurantNotificationRes;
import com.example.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantReviewRes extends BaseDTO {

    private long restaurantId;
    private long ownerId;
    private String name;
    private String category;
    private String content;
    private String phone;
    private int tablePersonMax;
    private int tablePersonMin;
    private String openTime;
    private String lastOrderTime;
    private String closeTime;
    private String address;
    private String detailAddress;
    private BigDecimal lat;
    private BigDecimal lng;
    private int lunchPrice;
    private int dinnerPrice;
    private long deposit;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;
    private List<GetRestaurantImageRes> images;
    private List<GetRestaurantNotificationRes> notifications;
    private List<GetFacilityRes> facilities;


}
