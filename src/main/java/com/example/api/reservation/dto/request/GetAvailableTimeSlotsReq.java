package com.example.api.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAvailableTimeSlotsReq {

    private long restaurantId;
    private int numberOfPeople;
    private LocalDate searchDate;
    private LocalTime visitTime;

    @Builder
    public GetAvailableTimeSlotsReq(long restaurantId, int numberOfPeople, LocalDate searchDate, LocalTime visitTime) {
        this.restaurantId = restaurantId;
        this.numberOfPeople = numberOfPeople;
        this.searchDate = searchDate;
        this.visitTime = visitTime;
    }
}
