package com.example.api.reservation.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationReq {

    @NotNull
    private LocalDateTime time;
    @NotNull
    @Min(1)
    private int numberOfPeople;
    private String memo;
}
