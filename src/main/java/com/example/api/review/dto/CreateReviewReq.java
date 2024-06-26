package com.example.api.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewReq {

    @Schema(hidden = true)
    private long memberId;
    private long reservationId;
    @Schema(description = "평점 : 1부터 5까지 정수만")
    private int rate;
    @Schema(description = "리뷰 내용")
    private String comment;

}