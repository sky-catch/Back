package com.example.api.reservation.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TimeSlots {

    private final List<TimeSlot> timeSlots;

    private TimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public static TimeSlots of(List<TimeSlot> timeSlots) {
        return new TimeSlots(timeSlots);
    }

    public TimeSlots subtract(TimeSlots other) {
        timeSlots.removeAll(other.getTimeSlots());
        return new TimeSlots(timeSlots);
    }
}
