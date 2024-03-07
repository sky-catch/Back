package com.example.api.reservation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationReq {

    private LocalDateTime time;
    private int numberOfPeople;
    private String memo;
}
