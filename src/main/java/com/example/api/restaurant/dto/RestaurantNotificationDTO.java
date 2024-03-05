package com.example.api.restaurant.dto;

import com.example.core.dto.BaseDTO;
import java.time.LocalDate;
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
public class RestaurantNotificationDTO extends BaseDTO {

    private long notificationId;
    private long restaurantId;
    private long ownerId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
}