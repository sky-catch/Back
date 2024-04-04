package com.example.api.reservation.dto;

import com.example.api.reservation.ReservationStatus;
import com.example.api.restaurant.dto.RestaurantDTO;
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
public class MyDetailReservationDTO extends BaseDTO {

    private long reservationId;
    private RestaurantDTO restaurant;
    private long memberId;
    private long paymentId;
    private LocalDateTime time;
    private int numberOfPeople;
    private String memo;
    private ReservationStatus status;

    public boolean isMine(long memberId) {
        return this.memberId == memberId;
    }
}
