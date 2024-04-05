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
public class ReservationAlarm extends BaseDTO {

    private long reservationAlarmId;
    private long reservationId;
    private ReservationAlarmType reservationAlarmType;
    private LocalDateTime scheduleTime;

    public ReservationAlarm(long reservationId, ReservationAlarmType reservationAlarmType, LocalDateTime scheduleTime) {
        this.reservationId = reservationId;
        this.reservationAlarmType = reservationAlarmType;
        this.scheduleTime = scheduleTime;
    }
}
