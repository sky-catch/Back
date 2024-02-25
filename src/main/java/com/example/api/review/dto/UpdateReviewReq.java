package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewReq extends BaseDTO {

    private long reviewId;
    @Schema(name= "평점", description = "1부터 5까지 정수만")
    private int rate;
    private String comment;

}