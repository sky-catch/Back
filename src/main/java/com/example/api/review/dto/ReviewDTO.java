package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO extends BaseDTO {

    private long reviewId;
    private long memberId;
    private long restaurantId;
    private long reservationId;
    private int rate;
    private String comment;
}