package com.example.api.alarm.reservation;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationAlarmMapper {

    void createReservationAlarm(ReservationAlarm reservationAlarm);

    List<GetReservationAlarm> getReservationAlarmListForMove();
}
