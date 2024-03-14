package com.example.api.restaurant.dto;

import com.example.api.holiday.Day;
import com.example.api.holiday.HolidayDTO;
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
public class RestaurantWithHolidayDTO extends BaseDTO {

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

    public boolean checkNumberOfPeople(int numberOfPeople) {
        return tablePersonMax < numberOfPeople || tablePersonMin > numberOfPeople;
    }

    public boolean checkHoliday(LocalDate date) {
        return holidays.stream()
                .anyMatch(holidayDTO -> holidayDTO.isSameDay(toDay(date)));
    }

    private Day toDay(LocalDate date) {
        String dateDayValue = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        return Day.findByValue(dateDayValue);
    }

    public RestaurantDTO toRestaurantDTO() {
        return RestaurantDTO.builder()
                .restaurantId(restaurantId)
                .ownerId(ownerId)
                .name(name)
                .category(category)
                .content(content)
                .phone(phone)
                .tablePersonMax(tablePersonMax)
                .tablePersonMin(tablePersonMin)
                .openTime(openTime)
                .lastOrderTime(lastOrderTime)
                .closeTime(closeTime)
                .address(address)
                .detailAddress(detailAddress)
                .lunchPrice(lunchPrice)
                .dinnerPrice(dinnerPrice)
                .savedCount(savedCount)
                .restaurantId(reviewCount)
                .reviewAvg(reviewAvg)
                .build();
    }
}
