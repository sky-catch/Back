package com.example.api.review;

import com.example.api.review.dto.GetReviewCommentRes;
import com.example.api.review.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ReviewMapper {
    boolean isExist(long reservationId);

    void createReview(ReviewDTO reviewDTO);

    ReviewDTO getReview(long reviewId);

    List<GetReviewCommentRes> getReviewCommentPage(@Param("restaurantId") long restaurantId, @Param("pageable") Pageable pageable);

    List<GetReviewCommentRes> getReviewByMember(long memberId);

    long getReviewCount(long restaurantId);

    void updateReview(ReviewDTO dto);

    void deleteReview(long reviewId);

}
