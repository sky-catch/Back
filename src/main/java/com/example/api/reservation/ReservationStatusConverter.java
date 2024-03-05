package com.example.api.reservation;

import org.springframework.core.convert.converter.Converter;

public class ReservationStatusConverter implements Converter<String, ReservationStatus> {

    @Override
    public ReservationStatus convert(String source) {
        return ReservationStatus.fromName(source);
    }
}
