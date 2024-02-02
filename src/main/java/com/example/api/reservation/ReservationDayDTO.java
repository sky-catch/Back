package com.example.api.reservation;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDayDTO extends BaseDTO {

    private long reservationDayId;
    private long restaurantId;
    private String day;
    private String time;
}
