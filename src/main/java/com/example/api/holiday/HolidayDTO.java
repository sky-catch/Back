package com.example.api.holiday;

import com.example.core.dto.BaseDTO;
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
public class HolidayDTO extends BaseDTO {

    private long holidayId;
    private long restaurantId;
    private Day day;

    public boolean isSameDay(Day day) {
        return this.day.isSameDay(day);
    }
}
