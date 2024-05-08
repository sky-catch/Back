package com.example.api.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReservationOfRestaurantRes {

    @Schema(description = "예약 리스트")
    private List<ReservationOfRestaurant> list;

    @Schema(description = "방문예정 예약 수")
    private int plannedCount;
    @Schema(description = "방문완료 예약 수")
    private int doneCount;
    @Schema(description = "취소/노쇼 예약 수")
    private int cancelCount;

    public GetReservationOfRestaurantRes(List<ReservationOfRestaurant> list, List<ReservationCount> reservationCount) {
        this.list = list;
        for (ReservationCount reservation : reservationCount) {
            if (reservation.getStatus().equals("PLANNED")) {
                plannedCount = reservation.getCount();
            } else if (reservation.getStatus().equals("DONE")) {
                doneCount = reservation.getCount();
            } else {
                cancelCount = reservation.getCount();
            }
        }
    }
}
