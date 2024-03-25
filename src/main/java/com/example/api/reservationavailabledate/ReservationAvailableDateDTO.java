package com.example.api.reservationavailabledate;

import com.example.api.restaurant.dto.UpdateRestaurantReq;
import com.example.core.dto.BaseDTO;
import java.time.LocalDate;
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
public class ReservationAvailableDateDTO extends BaseDTO {

    private long reservationAvailableDateId;
    private long restaurantId;
    private LocalDate beginDate;
    private LocalDate endDate;

    public ReservationAvailableDateDTO(UpdateRestaurantReq req) {
        this.restaurantId = req.getRestaurantId();
        this.beginDate = req.getReservationBeginDate();
        this.endDate = req.getReservationEndDate();
    }

    public boolean isValid(LocalDate date) {
        boolean isValidBeginDate = beginDate.isBefore(date) || beginDate.isEqual(date);
        boolean isValidEndDate = endDate.isEqual(date) || endDate.isAfter(date);

        return isValidBeginDate && isValidEndDate;
    }
}
