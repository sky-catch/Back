package com.example.api.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TimeSlot {

    @JsonFormat(pattern = "HH:mm")
    private final LocalTime time;

    @Builder
    public TimeSlot(LocalTime time) {
        this.time = time;
    }

    public static TimeSlot of(LocalTime time) {
        return new TimeSlot(time);
    }

    @JsonIgnore
    public TimeSlot getNextTimeSlot() {
        return new TimeSlot(time.plusMinutes(30));
    }

    public boolean isBeforeOrEqual(LocalTime other) {
        return time.isBefore(other) || time.equals(other);
    }

    public String toTimeString(){
        return time.toString();
    }
}
