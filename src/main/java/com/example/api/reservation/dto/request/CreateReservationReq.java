package com.example.api.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예약 생성 요청값")
public class CreateReservationReq {

    @NotNull
    @Schema(description = "방문일 + 방문 시간", example = "2024-03-13 10:00:00", type = "string")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime visitDateTime;

    @NotNull
    @Min(1)
    @Schema(description = "방문 인원 수", example = "2")
    private int numberOfPeople;

    @Schema(description = "메모", example = "창가 자리 부탁드려요.")
    private String memo;
}
