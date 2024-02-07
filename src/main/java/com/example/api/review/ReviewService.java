package com.example.api.review;

import com.example.api.review.dto.ReviewDTO;
import com.example.core.file.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;
    private final S3UploadService s3UploadService;

    //언더아머 log확인
    public void createReview(ReviewDTO dto, List<MultipartFile> files) throws IOException {
        //todo canCreateReview check reservation status
        //todo get restaurantId from reservation

        long restaurantId = 1L;

        //todo restaurant reviewCount, rate update
        ReviewDTO reviewDTO = ReviewDTO.builder().restaurantId(restaurantId).memberId(1L)
                .reservationId(dto.getReservationId()).rate(dto.getRate()).comment(dto.getComment()).build();

        //todo 외부 api가 실패한다면?
        List<String> fileNames = s3UploadService.upload(files);
        reviewMapper.createReview(reviewDTO);
        reviewImageMapper.createReviewImage(reviewDTO.getReviewId(), fileNames);
    }

    public void updateReview(ReviewDTO dto) {
        reviewMapper.updateReview(dto);
    }

    public void deleteReview(long reviewId) {
        //todo 사장 댓글 달려있는지 확인
        reviewMapper.deleteReview(reviewId);
    }
}
