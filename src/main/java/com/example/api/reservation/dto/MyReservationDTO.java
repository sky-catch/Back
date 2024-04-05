package com.example.api.reservation.dto;

import com.example.api.reservation.ReservationStatus;
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
public class MyReservationDTO extends BaseDTO {

    private long reservationId;
    private long restaurantId;
    private String restaurantName;
    private String restaurantCategory;
    private String restaurantAddress;
    private long memberId;
    private long paymentId;
    private LocalDateTime time;
    private int numberOfPeople;
    private String memo;
    private ReservationStatus status;
}
