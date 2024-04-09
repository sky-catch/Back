package com.example.api.alarm;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAlarm extends BaseDTO {

    private long alarmId;
    private long restaurantId;
    private String restaurantName;
    private AlarmType alarmType;
    private String message;
    private LocalDateTime reservationTime;
    private long reviewId;

}