package com.example.api.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예약 가능 시간 조회 요청값")
public class GetAvailableTimeSlotsReq {

    @NotNull
    @Min(1)
    @Schema(description = "식당 ID", example = "1")
    private long restaurantId;
    @NotNull
    @Min(1)
    @Schema(description = "예약 인원 수", example = "1")
    private int numberOfPeople;
    @NotNull
    @Schema(description = "방문일", example = "2024-03-13")
    private LocalDate searchDate;
    @NotNull
    @Schema(description = "방문 시간", example = "13:00")
    private LocalTime visitTime;
}
