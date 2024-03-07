package com.example.api.review;

import com.example.api.comment.CommentMapper;
import com.example.api.reservation.ReservationDTO;
import com.example.api.reservation.ReservationMapper;
import com.example.api.reservation.ReservationStatus;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.review.dto.CreateReviewReq;
import com.example.api.review.dto.ReviewDTO;
import com.example.api.review.dto.ReviewImageDTO;
import com.example.api.review.dto.UpdateReviewReq;
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
    private final ReservationMapper reservationMapper;
    private final CommentMapper commentMapper;
    private final S3UploadService s3UploadService;

    @Transactional
    public void createReview(CreateReviewReq dto, List<MultipartFile> files) {
        ReviewDTO reviewDTO = new ReviewDTO(dto);

        //todo forTest
        ReservationDTO reservationDTO = reservationMapper.getReservation(dto.getReservationId());
        /*if(reservationDTO.getStatus() != ReservationStatus.DONE){
            throw new SystemException("리뷰를 작성할 수 있는 상태가 아닙니다.");
        }
        if(reviewMapper.isExist(reviewDTO.getReservationId())){
            throw new SystemException("리뷰가 이미 존재합니다.");
        }*/

        reviewMapper.createReview(reviewDTO);
        if (!files.isEmpty()) {
            reviewImageMapper.createReviewImage(reviewDTO.getReviewId(), s3UploadService.upload(files));
        }

        restaurantMapper.increaseReviewCountAndRate(reviewDTO);
    }

    @Transactional
    public void updateReview(UpdateReviewReq dto, List<MultipartFile> files) {
        if(reviewMapper.getReview(dto.getReviewId()) == null){
            throw new SystemException("해당 리뷰가 없습니다.");
        }
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

        s3UploadService.delete(reviewImageMapper.getReviewImages(reviewId));
        restaurantMapper.decreaseReviewCountAndRate(reviewMapper.getReview(reviewId));
        reviewImageMapper.deleteReviewImages(reviewId);
        reviewMapper.deleteReview(reviewId);
    }
}
