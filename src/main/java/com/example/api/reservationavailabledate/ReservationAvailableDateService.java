package com.example.api.reservationavailabledate;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationAvailableDateService {

    private final ReservationAvailableDateMapper reservationAvailableDateMapper;

    @Transactional
    public void create(long restaurantId, LocalDate beginDate, LocalDate endDate) {
        ReservationAvailableDateDTO reservationAvailableDateDTO = ReservationAvailableDateDTO.builder()
                .restaurantId(restaurantId)
                .beginDate(beginDate)
                .endDate(endDate)
                .build();

        reservationAvailableDateMapper.save(reservationAvailableDateDTO);
    }

    @Transactional
    public void update(ReservationAvailableDateDTO dto) {
        reservationAvailableDateMapper.update(dto);
    }
}
