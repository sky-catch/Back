package com.example.api.review;

import com.example.api.comment.CommentMapper;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.review.dto.ReviewDTO;
import com.example.core.file.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;
    private final RestaurantMapper restaurantMapper;
    private final CommentMapper commentMapper;
    private final S3UploadService s3UploadService;

    @Transactional
    public void createReview(ReviewDTO reviewDTO, List<MultipartFile> files) throws IOException {
        //todo can Create Review check reservation status

        reviewDTO.setMemberId(1L);

        reviewMapper.createReview(reviewDTO);
        reviewImageMapper.createReviewImage(reviewDTO.getReviewId(), s3UploadService.upload(files));
        restaurantMapper.increaseReviewCountAndRate(reviewDTO);
    }
    
    @Transactional
    public void updateReview(ReviewDTO reviewDTO, List<MultipartFile> files) throws IOException {
        long reviewId = reviewDTO.getReviewId();

        s3UploadService.delete(reviewImageMapper.getReviewImages(reviewId));
        reviewImageMapper.deleteReviewImages(reviewId);

        reviewMapper.updateReview(reviewDTO);
        reviewImageMapper.createReviewImage(reviewId, s3UploadService.upload(files));
    }

    @Transactional
    public void deleteReview(long reviewId) {
        if(commentMapper.isPresentComment(reviewId)){
            //todo 사장 댓글이 달려있습니다. 에러 발생시키기
            return;
        }
        restaurantMapper.decreaseReviewCountAndRate(reviewMapper.getReview(reviewId));
        reviewImageMapper.deleteReviewImages(reviewId);
        reviewMapper.deleteReview(reviewId);
    }
}
