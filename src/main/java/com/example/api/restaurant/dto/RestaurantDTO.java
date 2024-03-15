package com.example.api.restaurant.dto;

import com.example.core.dto.BaseDTO;
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

    public boolean isOwner(long ownerId) {
        return this.ownerId == ownerId;
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
