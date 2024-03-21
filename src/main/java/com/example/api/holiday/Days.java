package com.example.api.holiday;

import com.example.core.exception.SystemException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class Days {

    private List<Day> days;

    private Days(List<Day> days) {
        this.days = days;
    }

    public static Days of(List<String> days) {
        if (days.size() != days.stream().distinct().count()) {
            throw new SystemException("중복값이 존재합니다.");
        }

        List<Day> dayList = days.stream()
                .map(Day::findByValue)
                .collect(Collectors.toList());
        return new Days(dayList);
    }
}
