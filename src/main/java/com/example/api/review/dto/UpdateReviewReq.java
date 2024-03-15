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
public class UpdateReviewReq {

    @Schema(description = "리뷰 ID")
    private long reviewId;
    @Schema(description = "평점 : 1부터 5까지 정수만")
    private int rate;
    @Schema(description = "리뷰 내용")
    private String comment;

}