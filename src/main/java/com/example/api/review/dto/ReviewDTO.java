package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
    private List<ReviewImageDTO> images;
}