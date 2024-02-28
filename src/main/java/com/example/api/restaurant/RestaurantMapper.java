package com.example.api.restaurant;

import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.review.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestaurantMapper {
    void increaseReviewCountAndRate(ReviewDTO reviewDTO);

    void decreaseReviewCountAndRate(ReviewDTO reviewDTO);

    void save(RestaurantDTO dto);

    void deleteAll();
}