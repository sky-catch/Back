package com.example.api.reservation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TimeSlots {

    private final List<TimeSlot> timeSlots;

    @Builder
    public TimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public static TimeSlots of(List<TimeSlot> timeSlots) {
        return new TimeSlots(timeSlots);
    }
}
