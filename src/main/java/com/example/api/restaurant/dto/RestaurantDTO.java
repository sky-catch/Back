package com.example.api.restaurant.dto;

import com.example.api.restaurant.dto.enums.HotPlace;
import com.example.api.restaurant.exception.RestaurantExceptionType;
import com.example.core.dto.BaseDTO;
import com.example.core.exception.SystemException;
import java.math.BigDecimal;
import java.time.LocalTime;
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
public class RestaurantDTO extends BaseDTO {

    private long restaurantId;
    private long ownerId;
    private String name;
    private String category;
    private String content;
    private String phone;
    private int tablePersonMax;
    private int tablePersonMin;
    private LocalTime openTime;
    private LocalTime lastOrderTime;
    private LocalTime closeTime;
    private String address;
    private String detailAddress;
    private String hotPlace;
    private BigDecimal lat;
    private BigDecimal lng;
    private int lunchPrice;
    private int dinnerPrice;
    private long deposit;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;

    public RestaurantDTO(CreateRestaurantReq req) {
        if (req.getTablePersonMax() < req.getTablePersonMin()) {
            throw new SystemException(RestaurantExceptionType.NOT_VALID_TABLE_PERSON);
        }
        this.ownerId = req.getOwnerId();
        this.name = req.getName();
        this.category = req.getCategory().getKoreanName();
        this.content = req.getContent();
        this.phone = req.getPhone();
        this.tablePersonMax = req.getTablePersonMax();
        this.tablePersonMin = req.getTablePersonMin();
        this.openTime = req.getOpenTime();
        this.lastOrderTime = req.getLastOrderTime();
        this.closeTime = req.getCloseTime();
        this.address = req.getAddress().getKoreanName();
        this.detailAddress = req.getDetailAddress();
        this.lunchPrice = req.getLunchPrice();
        this.dinnerPrice = req.getDinnerPrice();
        this.deposit = req.getDeposit();
        this.hotPlace = HotPlace.getHotPlaceValue(req.getDetailAddress());
        this.lat = req.getLat();
        this.lng = req.getLng();
    }

    public RestaurantDTO(UpdateRestaurantReq req) {
        if (req.getTablePersonMax() < req.getTablePersonMin()) {
            throw new SystemException(RestaurantExceptionType.NOT_VALID_TABLE_PERSON);
        }
        this.restaurantId = req.getRestaurantId();
        this.ownerId = req.getOwnerId();
        this.name = req.getName();
        this.category = req.getCategory().getKoreanName();
        this.content = req.getContent();
        this.phone = req.getPhone();
        this.tablePersonMax = req.getTablePersonMax();
        this.tablePersonMin = req.getTablePersonMin();
        this.openTime = req.getOpenTime();
        this.lastOrderTime = req.getLastOrderTime();
        this.closeTime = req.getCloseTime();
        this.address = req.getAddress().getKoreanName();
        this.detailAddress = req.getDetailAddress();
        this.lunchPrice = req.getLunchPrice();
        this.dinnerPrice = req.getDinnerPrice();
        this.deposit = req.getDeposit();
        this.lat = req.getLat();
        this.lng = req.getLng();
    }

    public boolean isOwner(long ownerId) {
        return this.ownerId == ownerId;
    }

    public void increaseSavedCount() {
        this.savedCount++;
    }

    public void decreaseSavedCount() {
        if (this.savedCount <= 0) {
            throw new SystemException(RestaurantExceptionType.NOT_VALID_SAVED_COUNT);
        }
        this.savedCount--;
    }

    public void update(UpdateRestaurantReq req) {
        this.name = req.getName();
        this.category = req.getCategory().getKoreanName();
        this.content = req.getContent();
        this.phone = req.getPhone();
        this.tablePersonMax = req.getTablePersonMax();
        this.tablePersonMin = req.getTablePersonMin();
        this.openTime = req.getOpenTime();
        this.lastOrderTime = req.getLastOrderTime();
        this.closeTime = req.getCloseTime();
        this.address = req.getAddress().getKoreanName();
        this.detailAddress = req.getDetailAddress();
        this.hotPlace = HotPlace.getHotPlaceValue(req.getDetailAddress());
        this.lat = req.getLat();
        this.lng = req.getLng();
        this.lunchPrice = req.getLunchPrice();
        this.dinnerPrice = req.getDinnerPrice();
        this.deposit = req.getDeposit();
    }

    // for mybatis
    public String getOpenTime() {
        return openTime.toString();
    }

    // for mybatis
    public String getLastOrderTime() {
        return lastOrderTime.toString();
    }

    // for mybatis
    public String getCloseTime() {
        return closeTime.toString();
    }
}
