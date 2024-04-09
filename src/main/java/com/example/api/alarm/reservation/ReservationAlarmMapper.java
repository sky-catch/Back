package com.example.api.alarm.reservation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationAlarmMapper {

    void createReservationAlarm(ReservationAlarm reservationAlarm);

    List<GetReservationAlarm> getReservationAlarmListForMove();

    void deleteReservationAlarm(@Param("reservationAlarmIdList") List<Long> reservationAlarmIdList);
}
