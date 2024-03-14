package com.example.api.holiday.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HolidayExceptionType {
    ALREADY_EXISTS("이미 존재하는 요일입니다."),
    ;

    private final String message;
}

