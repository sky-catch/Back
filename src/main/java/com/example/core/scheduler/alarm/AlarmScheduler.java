package com.example.core.scheduler.alarm;

import com.example.api.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmScheduler {

    private final AlarmService alarmService;

    @Scheduled(cron = "0 0/2 * * * *")    //2분마다 실행
    public void moveReservationAlarm() {
        int count = alarmService.moveReservationAlarm();
        log.debug("moveReservationAlarm : {}건 실행", count);
    }
}
