package com.example.api.alarm.reservation;

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
public class GetReservationAlarm extends BaseDTO {

    private long reservationAlarmId;
    private long restaurantId;
    private long memberId;
    private LocalDateTime reservationTime;
    private ReservationAlarmType reservationAlarmType;

}
