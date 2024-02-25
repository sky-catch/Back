package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class GetReviewRes extends BaseDTO {
    private long reviewId;
    private long restaurantId;
    private long reservationId;
    private String name;
    private String restaurantImage;
    private String category;
    private String address;
    private int rate;
    private String comment;
    private List<ReviewImageDTO> images;

}