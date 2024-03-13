package com.example.api.reservation.dto;

import com.example.api.restaurant.dto.RestaurantDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAvailableTimeSlotDTO {

    private RestaurantDTO restaurantDTO;
    private int numberOfPeople;
    private LocalDate searchDate;
    private LocalTime visitTime;

    @Builder
    public GetAvailableTimeSlotDTO(RestaurantDTO restaurantDTO, int numberOfPeople, LocalDate searchDate,
                                   LocalTime visitTime) {
        this.restaurantDTO = restaurantDTO;
        this.numberOfPeople = numberOfPeople;
        this.searchDate = searchDate;
        this.visitTime = visitTime;
    }

    public long getRestaurantId() {
        return restaurantDTO.getRestaurantId();
    }
}
