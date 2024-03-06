package com.example.api.restaurant.dto;

import com.example.api.restaurantnotification.dto.GetRestaurantNotificationRes;
import com.example.core.dto.BaseDTO;
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
public class GetRestaurantRes extends BaseDTO {

    private long restaurantId;
    private long ownerId;
    private String name;
    private String category;
    private String content;
    private String phone;
    private int capacity;
    private String openTime;
    private String lastOrderTime;
    private String address;
    private String detailAddress;
    private int lunchPrice;
    private int dinnerPrice;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;
    private List<RestaurantImage> images;
    private List<GetRestaurantNotificationRes> notifications;

    public void sortImages() {
        images = images.stream()
                .sorted((restaurantImage1, restaurantImage2) -> {
                    if (isSameType(restaurantImage1, restaurantImage2)) {
                        return sortById(restaurantImage1, restaurantImage2);
                    }
                    return sortByType(restaurantImage1, restaurantImage2);
                })
                .collect(Collectors.toList());
    }

    private boolean isSameType(RestaurantImage restaurantImage1, RestaurantImage restaurantImage2) {
        return restaurantImage1.getType() == restaurantImage2.getType();
    }

    private int sortById(RestaurantImage restaurantImage1, RestaurantImage restaurantImage2) {
        return (int) (restaurantImage1.getRestaurantId() - restaurantImage2.getRestaurantId());
    }

    private int sortByType(RestaurantImage restaurantImage1, RestaurantImage restaurantImage2) {
        return restaurantImage1.getType().compareTo(restaurantImage2.getType());
    }
}
