package com.example.api.reservationavailabledate;

import com.example.core.dto.BaseDTO;
import java.time.LocalDate;
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

    public boolean isValid(LocalDate date) {
        boolean isValidBeginDate = beginDate.isBefore(date) || beginDate.isEqual(date);
        boolean isValidEndDate = endDate.isEqual(date) || endDate.isAfter(date);

        return isValidBeginDate && isValidEndDate;
    }
}
