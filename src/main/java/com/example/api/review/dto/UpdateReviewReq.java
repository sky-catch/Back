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

    private long reviewId;
    @Schema(description = "평점 : 1부터 5까지 정수만")
    private int rate;
    private String comment;

}