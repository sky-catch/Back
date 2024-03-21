package com.example.api.reservation.dto.condition;

import com.example.api.reservation.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationSearchCond {

    private long restaurantId;
    private ReservationStatus status;
    private LocalDate searchDate;
    private LocalTime visitTime;

    @Builder
    public ReservationSearchCond(long restaurantId, ReservationStatus status, LocalDate searchDate,
                                 LocalTime visitTime) {
        this.restaurantId = restaurantId;
        this.status = status;
        this.searchDate = searchDate;
        this.visitTime = visitTime;
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
