package com.example.api.comment.dto;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO extends BaseDTO {

    private long commentId;
    private long reviewId;
    private long ownerId;
    private String content;

    public CommentDTO(CreateCommentReq dto) {
        this.reviewId = dto.getReviewId();
        this.content = dto.getContent();
    }

    public CommentDTO(UpdateCommentReq dto) {
        this.commentId = dto.getCommentId();
        this.content = dto.getContent();
    }
}