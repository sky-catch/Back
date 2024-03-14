package com.example.api.facility.dto;

import com.example.core.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Optional;
import java.util.stream.Stream;

public enum Facility {

    PARKING, VALET_PARKING, CORKAGE, CORKAGE_FREE, RENT, NO_KIDS, WINE_DELIVERY, LETTERING, SOMMELIER, PET, ACCESSIBLE;

    @JsonCreator
    public static Facility parsing(String inputValue){
        Optional<Facility> result = Stream.of(Facility.values())
                .filter(facility -> facility.toString().equals(inputValue.toUpperCase()))
                .findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new SystemException("유효하지 않은 facility입니다.");
        }
    }

}
