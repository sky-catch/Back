package com.example.api.restaurantimage;

import com.example.api.restaurantimage.dto.RestaurantImageDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestaurantImageMapper {
    void addRestaurantImages(long restaurantId, List<RestaurantImageDTO> list);

    void deleteRestaurantImages(long restaurantId);

    List<String> getRestaurantImagesBy(long restaurantId);

    void deleteReviewImages(long reviewId);
}
