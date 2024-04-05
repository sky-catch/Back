package com.example.api.alarm.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum ReservationAlarmType {

    DAY2("2일", 48), DAY1("1일", 24), TIME2("2시간", 2), TIME1("1시간", 1);

    private final String comment;
    private final int hours;

    public LocalDateTime calculateTime(LocalDateTime reservationTime){
        return reservationTime.minusHours(hours);
    }

}
