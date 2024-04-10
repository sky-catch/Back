package com.example.api.restaurant.dto;

import com.example.api.restaurant.exception.RestaurantExceptionType;
import com.example.core.dto.BaseDTO;
import com.example.core.exception.SystemException;
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
    private int lunchPrice;
    private int dinnerPrice;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;

    // todo 생성자 값 검증하기 - tablePersonMax < tablePersonMin

    public RestaurantDTO(CreateRestaurantReq req) {
        this.ownerId = req.getOwnerId();
        this.name = req.getName();
        this.category = req.getCategory();
        this.content = req.getContent();
        this.phone = req.getPhone();
        this.tablePersonMax = req.getTablePersonMax();
        this.tablePersonMin = req.getTablePersonMin();
        this.openTime = req.getOpenTime();
        this.lastOrderTime = req.getLastOrderTime();
        this.closeTime = req.getCloseTime();
        this.address = req.getAddress();
        this.detailAddress = req.getDetailAddress();
        this.lunchPrice = req.getLunchPrice();
        this.dinnerPrice = req.getDinnerPrice();
    }

    public RestaurantDTO(UpdateRestaurantReq req) {
        this.restaurantId = req.getRestaurantId();
        this.ownerId = req.getOwnerId();
        this.name = req.getName();
        this.category = req.getCategory();
        this.content = req.getContent();
        this.phone = req.getPhone();
        this.tablePersonMax = req.getTablePersonMax();
        this.tablePersonMin = req.getTablePersonMin();
        this.openTime = req.getOpenTime();
        this.lastOrderTime = req.getLastOrderTime();
        this.closeTime = req.getCloseTime();
        this.address = req.getAddress();
        this.detailAddress = req.getDetailAddress();
        this.lunchPrice = req.getLunchPrice();
        this.dinnerPrice = req.getDinnerPrice();
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
