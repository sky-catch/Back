package com.example.api.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예약 생성 요청값")
public class CreateReservationReq {

    // todo 프론트와 방문일 + 방문 시간 정하기
    @NotNull
    @Schema(description = "방문일 + 방문 시간", example = "2024-03-13")
    private LocalDateTime time;
    @NotNull
    @Min(1)
    @Schema(description = "방문 인원 수", example = "2")
    private int numberOfPeople;
    @Schema(description = "메모", example = "창가 자리 부탁드려요.")
    private String memo;
}
