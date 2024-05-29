package com.example.api.reservation.dto;

import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TimeSlots {

    private final List<TimeSlot> timeSlots;

    private TimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public static TimeSlots of(List<TimeSlot> timeSlots) {
        return new TimeSlots(timeSlots);
    }

    public static TimeSlots getWholeTodayTimeSlot(TimeSlot reservationTime, LocalTime lastOrderTime) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        while (reservationTime.isBeforeOrEqual(lastOrderTime) && !reservationTime.getTime().equals(LocalTime.MIDNIGHT)) {
            availableTimeSlots.add(reservationTime);
            reservationTime = reservationTime.getNextTimeSlot();
        }
        return new TimeSlots(availableTimeSlots);
    }

    public TimeSlots subtract(TimeSlots other) {
        timeSlots.removeAll(other.getTimeSlots());
        return new TimeSlots(timeSlots);
    }

    public TimeSlots limit3() {
        return TimeSlots.of(timeSlots.stream().limit(3).collect(Collectors.toList()));
    }

    public List<String> toTimeString() {
        return timeSlots.stream().map(TimeSlot::toTimeString).collect(Collectors.toList());
    }

}
