package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAndRestaurantAndImageDTO extends BaseDTO {

    private long reviewId;
    private long memberId;
    private long restaurantId;
    private String restaurantName;
    private long reservationId;
    private int rate;
    private String comment;
    private List<ReviewImageDTO> images;
}