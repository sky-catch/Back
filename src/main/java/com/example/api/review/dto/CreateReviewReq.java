package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewReq extends BaseDTO {

    private long restaurantId;
    private long reservationId;
    @Schema(name= "평점", description = "1부터 5까지 정수만")
    private int rate;
    private String comment;

}