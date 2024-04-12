package com.example.api.member.dto;

import com.example.api.review.dto.ReviewDTO;
import com.example.api.savedrestaurant.SavedRestaurantDTO;
import com.example.core.dto.HumanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "마이 페이지 회원 정보 조회 응답값")
@Getter
@NoArgsConstructor
public class MyMainRes {

    @Schema(description = "닉네임", example = "닉네임입니다.")
    private String nickname;
    @Schema(description = "닉네임", example = "http://k.kakaocdn.net/파일경로.jpg")
    private String profileImageUrl;
    @Schema(description = "이름", example = "이름입니다.")
    private String name;
    @Schema(description = "계정 활성화 상태", example = "ACTIVE")
    private HumanStatus status;
    @Schema(description = "저장한 식당들")
    private List<SavedRestaurantDTO> savedRestaurants;
    @Schema(description = "작성한 리뷰들")
    private List<ReviewDTO> reviews;

    @Builder
    public MyMainRes(String nickname, String profileImageUrl, String name, HumanStatus status,
                     List<SavedRestaurantDTO> savedRestaurants, List<ReviewDTO> reviews) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.status = status;
        this.savedRestaurants = savedRestaurants;
        this.reviews = reviews;
    }
}
