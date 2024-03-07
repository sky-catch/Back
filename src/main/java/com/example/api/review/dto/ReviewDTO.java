package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

    public ReviewDTO(CreateReviewReq dto) {
        this.memberId = dto.getMemberId();
        this.restaurantId = dto.getRestaurantId();
        this.reservationId = dto.getReservationId();
        this.rate = dto.getRate();
        this.comment = dto.getComment();
    }

    public ReviewDTO(UpdateReviewReq dto) {
        this.reviewId = dto.getReviewId();
        this.rate = dto.getRate();
        this.comment = dto.getComment();
    }
}