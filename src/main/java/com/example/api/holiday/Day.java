package com.example.api.holiday;

import com.example.core.exception.SystemException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Day {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일");

    private final String value;

    public static Day findByValue(String value) {
        return Arrays.stream(values())
                .filter(day -> day.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new SystemException("존재하지 않는 요일입니다."));
    }

    public static Day findByName(String name) {
        return Arrays.stream(values())
                .filter(day -> day.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new SystemException("존재하지 않는 요일입니다."));
    }
}
