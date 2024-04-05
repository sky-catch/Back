package com.example.api.alarm;

import com.example.api.alarm.reservation.GetReservationAlarm;
import com.example.api.alarm.reservation.ReservationAlarm;
import com.example.api.alarm.reservation.ReservationAlarmMapper;
import com.example.api.alarm.reservation.ReservationAlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final ReservationAlarmMapper reservationAlarmMapper;
    private final AlarmMapper alarmMapper;

    public void createReservationAlarm(long reservationId, LocalDateTime reservationTime){
        Stream.of(ReservationAlarmType.values())
                .forEach(type -> {
                    ReservationAlarm reservationAlarm = new ReservationAlarm(reservationId, type, type.calculateTime(reservationTime));
                    reservationAlarmMapper.createReservationAlarm(reservationAlarm);
                });
        //todo 기존 ReservationAlarm 삭제
    }

    public void moveReservationAlarm(){
        List<GetReservationAlarm> reservationAlarmList = reservationAlarmMapper.getReservationAlarmListForMove();
        for (GetReservationAlarm i : reservationAlarmList) {
            alarmMapper.creatAlarm(new Alarm(i));
        }
    }
}
