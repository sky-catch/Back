package com.example.api.review;

import com.example.api.member.MemberDTO;
import com.example.api.review.dto.CreateReviewReq;
import com.example.api.review.dto.UpdateReviewReq;
import com.example.core.exception.SystemException;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Tag(name = "리뷰")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "리뷰 생성")
    public void createReview(@RequestPart CreateReviewReq createReviewReq,
                             @Parameter(description = "이미지 형식의 파일만 가능, 최대 5개")
                             @RequestPart(required = false) List<MultipartFile> files,
                             @LoginMember MemberDTO memberDTO) {
        createReviewReq.setMemberId(memberDTO.getMemberId());
        if (files.size() > 5) {
            throw new SystemException("이미지 개수는 5개를 넘을 수 없습니다.");
        }
        reviewService.createReview(createReviewReq, files);
    }

    @PutMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(summary = "리뷰 수정")
    public void updateReview(@RequestPart UpdateReviewReq updateReviewReq,
                             @Parameter(description = "파일 전송하지 않을 시 - 기존 리뷰의 모든 이미지 삭제 <br>" +
                                     "일부만 삭제하고 싶은 경우 - 원하는 이미지 파일만 보내기 <br>일부는 삭제하고 또다른 이미지 추가가 있을 경우 - 삭제를 제외한 나머지 파일 + 추가 파일")
                             @RequestPart(required = false) List<MultipartFile> files,
                             @LoginMember MemberDTO memberDTO) {
        if (files.size() > 5) {
            throw new SystemException("이미지 개수는 5개를 넘을 수 없습니다.");
        }
        reviewService.updateReview(updateReviewReq, files);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "리뷰 삭제", description = "사장 답글이 달려 있을시 삭제 불가")
    public void deleteReview(@PathVariable(name = "id") long reviewId, @LoginMember MemberDTO memberDTO) {
        reviewService.deleteReview(reviewId);
    }
}
