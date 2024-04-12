package com.example.api.restaurant;

import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantWithHolidayAndAvailableDateDTO;
import com.example.api.review.dto.ReviewDTO;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestaurantMapper {
    void increaseReviewCountAndRate(ReviewDTO reviewDTO);

    void decreaseReviewCountAndRate(ReviewDTO reviewDTO);

    void save(RestaurantDTO dto);

    Optional<RestaurantDTO> findById(long restaurantId);

    Optional<GetRestaurantRes> findRestaurantInfoById(long restaurantId);

    boolean isAlreadyCreated(long ownerId);

    boolean isAlreadyExistsName(String name);

    boolean isAlreadyExistsNameExcludeSelf(String name, long restaurantId);

    Optional<GetRestaurantRes> findRestaurantInfoByName(String name);

    Optional<RestaurantWithHolidayAndAvailableDateDTO> findRestaurantWithHolidayAndAvailableDateById(long restaurantId);

    void updateRestaurant(RestaurantDTO dto);

    Optional<GetRestaurantRes> findRestaurantInfoByOwnerId(long ownerId);

    void increaseSavedCount(RestaurantDTO restaurant);

    void decreaseSavedCount(RestaurantDTO restaurant);

    // for test
    List<RestaurantDTO> findAll();
}