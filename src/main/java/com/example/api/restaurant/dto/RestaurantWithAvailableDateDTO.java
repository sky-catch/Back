package com.example.api.restaurant.dto;

import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.core.dto.BaseDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
public class RestaurantWithAvailableDateDTO extends BaseDTO {

    private long restaurantId;
    private long ownerId;
    private String name;
    private String category;
    private String content;
    private String phone;
    private int tablePersonMax;
    private int tablePersonMin;
    private LocalTime openTime;
    private LocalTime lastOrderTime;
    private LocalTime closeTime;
    private String address;
    private String detailAddress;
    private BigDecimal lat;
    private BigDecimal lng;
    private int lunchPrice;
    private int dinnerPrice;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;
    private ReservationAvailableDateDTO availableDate;

    public boolean isOutboundTablePerson(int numberOfPeople) {
        return tablePersonMax < numberOfPeople || tablePersonMin > numberOfPeople;
    }

    public boolean isNotValidVisitTime(LocalTime time) {
        return openTime.isAfter(time) || lastOrderTime.isBefore(time);
    }

    public boolean isNotAvailableDate(LocalDate date) {
        return !availableDate.isValid(date);
    }
}
