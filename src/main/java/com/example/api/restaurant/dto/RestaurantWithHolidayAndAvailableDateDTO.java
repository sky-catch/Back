package com.example.api.restaurant.dto;

import com.example.api.holiday.Day;
import com.example.api.holiday.HolidayDTO;
import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.core.dto.BaseDTO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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
public class RestaurantWithHolidayAndAvailableDateDTO extends BaseDTO {

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
    private int lunchPrice;
    private int dinnerPrice;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;
    private List<HolidayDTO> holidays;
    private ReservationAvailableDateDTO availableDate;

    public boolean isOutboundTablePerson(int numberOfPeople) {
        return tablePersonMax < numberOfPeople || tablePersonMin > numberOfPeople;
    }

    public boolean isHoliday(LocalDate date) {
        if (hasNoHoliday()) {
            return false;
        }

        return holidays.stream()
                .anyMatch(holidayDTO -> holidayDTO.isSameDay(toDay(date)));
    }

    private boolean hasNoHoliday() {
        return holidays.stream()
                .anyMatch(holidayDTO -> holidayDTO.getDay() == null);
    }

    private Day toDay(LocalDate date) {
        String dateDayValue = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        return Day.findByValue(dateDayValue);
    }

    public boolean isNotValidVisitTime(LocalTime time) {
        return openTime.isAfter(time) || lastOrderTime.isBefore(time);
    }

    public boolean isNotAvailableDate(LocalDate date) {
        return !availableDate.isValid(date);
    }
}
