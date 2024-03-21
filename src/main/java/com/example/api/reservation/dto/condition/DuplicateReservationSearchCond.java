package com.example.api.reservation.dto.condition;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DuplicateReservationSearchCond {

    private long restaurantId;
    private long memberId;
    private LocalDateTime time;

    @Builder
    public DuplicateReservationSearchCond(long restaurantId, long memberId, LocalDateTime time) {
        this.restaurantId = restaurantId;
        this.memberId = memberId;
        this.time = time;
    }

    // for mybatis
    public String getTime() {
        return time.toString();
    }
}
