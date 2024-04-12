package com.example.core.scheduler.reservation;

import com.example.api.reservation.ReservationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;

    // 개발을 위해 주석 처리
//    @Scheduled(cron = "0 0 0 * * *")    // 00:00마다 실행
    public void changeReservationStatusToNoShowByDate() {
        List<Long> noShowIds = reservationService.changeReservationsToNoShowByDate(LocalDate.now());
        log.debug("changeReservationStatusToNoShowByAuto noShowIds: {}", noShowIds);
        log.debug("changeReservationStatusToNoShowByAuto : {}건 실행", noShowIds.size());
    }
}
