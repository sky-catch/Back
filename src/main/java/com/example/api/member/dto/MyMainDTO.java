package com.example.api.member.dto;

import com.example.api.review.dto.ReviewDTO;
import com.example.api.savedrestaurant.SavedRestaurantDTO;
import com.example.core.dto.HumanStatus;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyMainDTO {

    private String nickname;
    private String profileImageUrl;
    private String name;
    private String email;
    private HumanStatus status;
    private List<SavedRestaurantDTO> savedRestaurants;
    private List<ReviewDTO> reviews;

    @Builder
    public MyMainDTO(String nickname, String profileImageUrl, String name, String email, HumanStatus status,
                     List<SavedRestaurantDTO> savedRestaurants, List<ReviewDTO> reviews) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.email = email;
        this.status = status;
        this.savedRestaurants = savedRestaurants;
        this.reviews = reviews;
    }
}
