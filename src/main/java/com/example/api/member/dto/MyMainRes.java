package com.example.api.member.dto;

import com.example.api.review.dto.ReviewDTO;
import com.example.api.savedrestaurant.SavedRestaurantDTO;
import com.example.core.dto.HumanStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyMainRes {

    private String nickname;
    private String profileImageUrl;
    private String name;
    private HumanStatus status;
    private List<SavedRestaurantDTO> savedRestaurants;
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
