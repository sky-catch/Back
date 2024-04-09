package com.example.api.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "노쇼로 바꾸고 싶은 예약 요청값")
public class ChangeReservationsStatusToNoShowReq {

    @NotNull
    @Schema(description = "노쇼로 바꾸고 싶은 예약 번호들", example = "[1,2,3,4,5]")
    private List<Long> noShowIds;
}
