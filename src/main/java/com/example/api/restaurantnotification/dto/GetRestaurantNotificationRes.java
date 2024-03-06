package com.example.api.restaurantnotification.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class GetRestaurantNotificationRes extends BaseDTO {

    private long notificationId;
    @JsonIgnore
    private long restaurantId;
    @JsonIgnore
    private long ownerId;
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
}