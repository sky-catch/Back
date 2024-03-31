package com.example.api.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class GetReviewCommentRes {
    private long reviewId;
    private long memberId;
    private String nickname;
    private String imagePath;
    private int rate;
    private String reviewContent;
    private List<ReviewImageDTO> images;
    private LocalDateTime reviewCreatedDate;
    private LocalDateTime reviewUpdatedDate;

    private long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedDate;
    private LocalDateTime commentUpdatedDate;
}