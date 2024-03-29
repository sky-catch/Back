package com.example.api.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "결제 요청값")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentCallbackRequest {

    @NotBlank
    @Schema(description = "고객사 식별코드", example = "imp_00000000")
    private String impUid;
    @NotNull
    @Schema(description = "예약 ID", example = "1")
    private long reservationId;
}
