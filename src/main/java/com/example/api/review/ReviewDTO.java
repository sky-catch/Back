package com.example.api.review;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO extends BaseDTO {

    private long reviewId;
    private long memberId;
    private long restaurantId;
    private int rate;
    private String comment;
}