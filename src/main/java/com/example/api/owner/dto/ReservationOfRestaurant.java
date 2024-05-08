package com.example.api.owner.dto;

import com.example.api.reservation.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationOfRestaurant {

    @Schema(description = "예약 ID", example = "1")
    private long reservationId;
    @Schema(description = "예약 시간", example = "2024-05-13T11:00:00")
    private LocalDateTime time;
    @Schema(description = "회원 ID", example = "1")
    private long memberId;
    @Schema(description = "회원 이름", example = "홍길동")
    private String memberName;
    @Schema(description = "예약 인원 수", example = "2")
    private int numberOfPeople;
    @Schema(description = "예약 메모", example = "창가자리 부탁드려요.")
    private String memo;
    @Schema(description = "예약 상태", example = "PLANNED")
    private ReservationStatus status;

}
