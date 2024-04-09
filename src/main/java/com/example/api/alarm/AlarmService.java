package com.example.api.alarm;

import com.example.api.alarm.reservation.GetReservationAlarm;
import com.example.api.alarm.reservation.ReservationAlarm;
import com.example.api.alarm.reservation.ReservationAlarmMapper;
import com.example.api.alarm.reservation.ReservationAlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AlarmService {
    /**
     * 다른 service DI하지 않기.
     */
    private final ReservationAlarmMapper reservationAlarmMapper;
    private final AlarmMapper alarmMapper;

    @Transactional
    public void createReservationAlarm(long reservationId, LocalDateTime reservationTime){
        Stream.of(ReservationAlarmType.values())
                .forEach(type -> {
                    ReservationAlarm reservationAlarm = new ReservationAlarm(reservationId, type, type.calculateTime(reservationTime));
                    reservationAlarmMapper.createReservationAlarm(reservationAlarm);
                });
    }

    @Transactional
    public int moveReservationAlarm(){
        List<GetReservationAlarm> reservationAlarmList = reservationAlarmMapper.getReservationAlarmListForMove();
        if(!reservationAlarmList.isEmpty()){
            List<Long> reservationAlarmIdList = reservationAlarmList.stream().map(GetReservationAlarm::getReservationAlarmId).collect(Collectors.toList());
            for (GetReservationAlarm i : reservationAlarmList) {
                alarmMapper.creatAlarm(new Alarm(i));
            }
            reservationAlarmMapper.deleteReservationAlarm(reservationAlarmIdList);
        }
        return reservationAlarmList.size();
    }
}
