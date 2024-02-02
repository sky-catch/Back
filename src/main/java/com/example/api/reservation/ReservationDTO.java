package com.example.api.reservation;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO extends BaseDTO {

    private long reservationId;
    private long restaurantId;
    private long memberId;
    private long reservationDayId;
    private LocalDateTime paymentId;
    private String time;
    private String numberOfPeople;
    private String memo;
    private String status;
}
