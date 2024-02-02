package com.example.api.restaurant.dto;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantNotificationDTO extends BaseDTO {

    private long notificationId;
    private long restaurantId;
    private long ownerId;
    private String title;
    private String content;
}