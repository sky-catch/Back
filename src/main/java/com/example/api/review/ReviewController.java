package com.example.api.review;

import com.example.api.review.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public void createReview(@RequestPart ReviewDTO dto, @RequestPart(required = false) List<MultipartFile> files) throws IOException {
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
