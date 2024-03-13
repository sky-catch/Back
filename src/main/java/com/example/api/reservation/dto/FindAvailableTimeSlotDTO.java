package com.example.api.reservation.dto;

import com.example.api.reservation.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindAvailableTimeSlotDTO {

    private long restaurantId;
    private LocalDate searchDate;
    private LocalTime visitTime;
    private ReservationStatus status;

    @Builder
    public FindAvailableTimeSlotDTO(long restaurantId, LocalDate searchDate, LocalTime visitTime,
                                    ReservationStatus status) {
        this.restaurantId = restaurantId;
        this.searchDate = searchDate;
        this.visitTime = visitTime;
        this.status = status;
    }

    // for mybatis
    public String getSearchDate() {
        return searchDate.toString();
    }

    // for mybatis
    public String getVisitTime() {
        return visitTime.toString();
    }
}
