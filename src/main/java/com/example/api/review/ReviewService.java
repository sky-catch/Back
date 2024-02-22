package com.example.api.review;

import com.example.api.comment.CommentMapper;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.review.dto.CreateReviewReq;
import com.example.api.review.dto.ReviewDTO;
import com.example.api.review.dto.ReviewImageDTO;
import com.example.core.exception.SystemException;
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
    public void createReview(CreateReviewReq dto, List<MultipartFile> files) throws IOException {
        //todo 예약 상태가 review 작성가능한 상태인지

        ReviewDTO reviewDTO = new ReviewDTO(dto);

        reviewDTO.setMemberId(1L);
        //todo forTest
        /*if(reviewMapper.isExist(reviewDTO.getReservationId())){
            throw new SystemException("리뷰가 이미 존재합니다.");
        }*/

        reviewMapper.createReview(reviewDTO);
        if (!files.isEmpty()) {
            reviewImageMapper.createReviewImage(reviewDTO.getReviewId(), s3UploadService.upload(files));
        }

        restaurantMapper.increaseReviewCountAndRate(reviewDTO);
    }

    @Transactional
    public void updateReview(CreateReviewReq dto, List<MultipartFile> files) throws IOException {
        ReviewDTO reviewDTO = new ReviewDTO(dto);
        long reviewId = reviewDTO.getReviewId();

        s3UploadService.delete(reviewImageMapper.getReviewImages(reviewId));
        reviewImageMapper.deleteReviewImages(reviewId);

        reviewMapper.updateReview(reviewDTO);
        reviewImageMapper.createReviewImage(reviewId, s3UploadService.upload(files));
    }

    @Transactional
    public void deleteReview(long reviewId) {
        if (commentMapper.isPresentComment(reviewId)) {
            throw new SystemException("사장의 댓글이 달려있어 삭제할 수 없습니다.");
        }
        restaurantMapper.decreaseReviewCountAndRate(reviewMapper.getReview(reviewId));
        reviewImageMapper.deleteReviewImages(reviewId);
        reviewMapper.deleteReview(reviewId);
    }
}
