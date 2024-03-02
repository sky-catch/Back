package com.example.api.restaurantImage;

import com.example.api.restaurantImage.dto.AddRestaurantImageWithTypeDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestaurantImageMapper {
    void addRestaurantImages(long restaurantId, List<AddRestaurantImageWithTypeDTO> list);

    List<String> getReviewImages(long reviewId);

    void deleteReviewImages(long reviewId);

}