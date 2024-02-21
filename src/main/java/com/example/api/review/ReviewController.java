package com.example.api.review;

import com.example.api.review.dto.CreateReviewReq;
import com.example.core.exception.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Tag(name = "리뷰")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "리뷰 생성", description = "현재 1번 멤버가 생성하도록 한 상태")
    public void createReview(@ParameterObject @RequestPart CreateReviewReq dto,
                             @Parameter(description = "이미지 형식의 파일만 가능, 최대 5개")
                             @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        if (files.size() > 5) {
            throw new SystemException("이미지 개수는 5개를 넘을 수 없습니다.");
        }
        reviewService.createReview(dto, files);
    }

    @PutMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "리뷰 수정")
    public void updateReview(@RequestPart CreateReviewReq dto,
                             @Parameter(description = "이미지 형식의 파일만 가능, 최대 5개")
                             @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        if (files.size() > 5) {
            throw new SystemException("이미지 개수는 5개를 넘을 수 없습니다.");
        }
        reviewService.updateReview(dto, files);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "리뷰 삭제", description = "사장 답글이 달려 있을시 삭제 불가")
    public void deleteReview(@PathVariable(name = "id") long reviewId) {
        reviewService.deleteReview(reviewId);
    }

}
