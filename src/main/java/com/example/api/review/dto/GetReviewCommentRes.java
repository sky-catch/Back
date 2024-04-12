package com.example.api.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "식당 리뷰 응답값")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewCommentRes {
    @Schema(description = "리뷰 ID", example = "1")
    private long reviewId;
    @Schema(description = "회원 ID", example = "1")
    private long memberId;
    @Schema(description = "회원 닉네임", example = "회원 닉네임입니다.")
    private String nickname;
    @Schema(description = "회원 이미지 경로", example = "http://k.kakaocdn.net/dn/파일이름.jpg")
    private String imagePath;
    @Schema(description = "평점 1~5", example = "1")
    private int rate;
    @Schema(description = "리뷰 내용", example = "Excellent service!")
    private String reviewContent;
    @Schema(description = "리뷰 이미지들")
    private List<ReviewImageDTO> images;
    private LocalDateTime reviewCreatedDate;
    private LocalDateTime reviewUpdatedDate;

    @Schema(description = "사장님 답글 ID", example = "1")
    private long commentId;
    @Schema(description = "사장님 답글 내용", example = "리뷰 감사합니다.")
    private String commentContent;
    private LocalDateTime commentCreatedDate;
    private LocalDateTime commentUpdatedDate;
}