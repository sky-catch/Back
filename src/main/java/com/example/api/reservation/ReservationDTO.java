package com.example.api.reservation;

import com.example.core.dto.BaseDTO;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO extends BaseDTO {

    private long reservationId;
    private long restaurantId;
    private long memberId;
    private long paymentId;
    private LocalDateTime time;
    private int numberOfPeople;
    private String memo;
    private ReservationStatus status;
}
