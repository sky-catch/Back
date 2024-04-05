package com.example.api.alarm;

import com.example.api.alarm.reservation.GetReservationAlarm;
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
public class Alarm extends BaseDTO {

    private long alarmId;
    private long memberId;
    private long restaurantId;
    private AlarmType alarmType;
    private String message;
    private LocalDateTime reservationTime;
    private long reviewId;

    public Alarm(GetReservationAlarm getReservationAlarm) {
        this.memberId = getReservationAlarm.getMemberId();
        this.alarmType = AlarmType.RESERVATION;
        this.message = "예약 " + getReservationAlarm.getReservationAlarmType().getComment() + "전 입니다.";
        this.reservationTime = getReservationAlarm.getReservationTime();
        this.restaurantId = getReservationAlarm.getRestaurantId();
    }

    public Alarm(long memberId, AlarmType alarmType, String message, long restaurantId) {
        this.memberId = memberId;
        this.alarmType = alarmType;
        this.message = message;
        this.restaurantId = restaurantId;
    }

    public Alarm(long memberId, AlarmType alarmType, String message, long restaurantId, long reviewId) {
        this.memberId = memberId;
        this.alarmType = alarmType;
        this.message = message;
        this.restaurantId = restaurantId;
        this.reviewId = reviewId;
    }
}