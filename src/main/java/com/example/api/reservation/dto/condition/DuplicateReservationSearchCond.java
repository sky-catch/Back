package com.example.api.reservation.dto.condition;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DuplicateReservationSearchCond {

    private long restaurantId;
    private LocalDateTime time;

    @Builder
    public DuplicateReservationSearchCond(long restaurantId, LocalDateTime time) {
        this.restaurantId = restaurantId;
        this.time = time;
    }

    // for mybatis
    public String getTime() {
        return time.toString();
    }
}
