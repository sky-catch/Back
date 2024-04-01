package com.example.api.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewCommentRes {
    @Schema(description = "리뷰 ID")
    private long reviewId;
    @Schema(description = "회원 ID")
    private long memberId;
    @Schema(description = "회원 닉네임")
    private String nickname;
    @Schema(description = "회원 이미지 경로")
    private String imagePath;
    @Schema(description = "평점 1~5")
    private int rate;
    @Schema(description = "리뷰 내용")
    private String reviewContent;
    @Schema(description = "리뷰 이미지들")
    private List<ReviewImageDTO> images;
    private LocalDateTime reviewCreatedDate;
    private LocalDateTime reviewUpdatedDate;

    @Schema(description = "사장님 답글 ID")
    private long commentId;
    @Schema(description = "사장님 답글 내용")
    private String commentContent;
    private LocalDateTime commentCreatedDate;
    private LocalDateTime commentUpdatedDate;
}