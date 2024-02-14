package com.example.api.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class ReviewCommentDTO{
    private long reviewId;
    private long memberId;
    private String nickname;
    private String imagePath;
    private int rate;
    private String comment;
    private List<ReviewImageDTO> images;
    private LocalDateTime reviewCreatedDate;
    private LocalDateTime reviewUpdatedDate;

    private long commentId;
    private String content;
    private LocalDateTime commentCreatedDate;
    private LocalDateTime commentUpdatedDate;
}