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

    /**
     * 예약 알람 만들기
     */
    public Alarm(GetReservationAlarm getReservationAlarm) {
        this.memberId = getReservationAlarm.getMemberId();
        this.alarmType = AlarmType.RESERVATION;
        this.message = "예약 " + getReservationAlarm.getReservationAlarmType().getComment() + "전 입니다.";
        this.reservationTime = getReservationAlarm.getReservationTime();
        this.restaurantId = getReservationAlarm.getRestaurantId();
    }

    /**
     * 리뷰 알람 만들기
     */
    public Alarm(long reviewId) {
        this.alarmType = AlarmType.RESERVATION;
        this.message = "리뷰에 답변이 달렸습니다.";
        this.reviewId = reviewId;
    }
}