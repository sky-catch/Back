package com.example.api.review;

import com.example.api.review.dto.ReviewDTO;
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
    public void createReview(@ParameterObject @RequestPart ReviewDTO dto,
                             @Parameter(description = "이미지 형식의 파일만 가능") @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        reviewService.createReview(dto, files);
    }

    @PutMapping("")
    public void updateReview(@RequestPart ReviewDTO dto, @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        reviewService.updateReview(dto, files);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable(name = "id") long reviewId) {
        reviewService.deleteReview(reviewId);
    }

}
