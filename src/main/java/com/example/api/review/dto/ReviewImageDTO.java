package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDTO extends BaseDTO {

    private long reviewImageId;
    private long reviewId;
    private String path;
}