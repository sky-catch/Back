package com.example.api.restaurant.dto;

import com.example.api.facility.dto.GetFacilityRes;
import com.example.api.restaurantnotification.dto.GetRestaurantNotificationRes;
import com.example.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
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
public class GetRestaurantInfoRes extends BaseDTO {

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
    private boolean isSaved;
    private List<GetRestaurantImageRes> images;
    private List<GetRestaurantNotificationRes> notifications;
    private List<GetFacilityRes> facilities;

    public void sortImages() {
        images = images.stream()
                .sorted((getRestaurantImageRes1, getRestaurantImageRes2) -> {
                    if (isSameType(getRestaurantImageRes1, getRestaurantImageRes2)) {
                        return sortById(getRestaurantImageRes1, getRestaurantImageRes2);
                    }
                    return sortByType(getRestaurantImageRes1, getRestaurantImageRes2);
                })
                .collect(Collectors.toList());
    }

    private boolean isSameType(GetRestaurantImageRes dto1, GetRestaurantImageRes dto2) {
        return dto1.getType() == dto2.getType();
    }

    private int sortById(GetRestaurantImageRes dto1, GetRestaurantImageRes dto2) {
        return (int) (dto1.getRestaurantId() - dto2.getRestaurantId());
    }

    private int sortByType(GetRestaurantImageRes dto1, GetRestaurantImageRes dto2) {
        return dto1.getType().compareTo(dto2.getType());
    }
}
