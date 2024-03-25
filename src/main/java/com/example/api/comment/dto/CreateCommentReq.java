package com.example.api.comment.dto;

import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentReq extends BaseDTO {

    private long reviewId;
    @Schema(description = "답글 내용")
    private String content;
}