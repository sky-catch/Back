package com.example.api.mydining;

import com.example.api.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class GetMyReservationDTO {
    private final long memberId;
    private final ReservationStatus status;

    @Builder
    public GetMyReservationDTO(long memberId, ReservationStatus status) {
        this.memberId = memberId;
        this.status = status;
    }
}
