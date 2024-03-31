package com.example.api.review;

import com.example.api.review.dto.GetReviewCommentRes;
import com.example.api.review.dto.GetReviewRes;
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

    //List<GetReviewCommentRes> getReviewCommentPage(@Param("restaurantId") long restaurantId, @Param("pageable") Pageable pageable);

    //todo 이거를 레스토랑 response에 추가 근데 GetReviewCommentRes이거 comment랑 review 분리해야할수도
    List<GetReviewCommentRes> getReviewComments(long restaurantId);

    List<GetReviewRes> getReviewByMember(long memberId);

    long getReviewCount(long restaurantId);

    void updateReview(ReviewDTO dto);

    void deleteReview(long reviewId);

}
