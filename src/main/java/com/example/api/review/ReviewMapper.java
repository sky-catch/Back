package com.example.api.review;

import com.example.api.review.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    void createReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getReviewsByRestaurant(long restaurantId);

    List<ReviewDTO> getReviewsByMember(long memberId);

    void updateReview(ReviewDTO dto);

    void deleteReview(long reviewId);

}
