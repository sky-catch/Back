package com.example.api.restaurant;

import com.example.api.restaurant.dto.*;
import com.example.api.restaurant.dto.search.GetRestaurantSearchListRes;
import com.example.api.restaurant.dto.search.RestaurantSummaryDTO;
import com.example.api.restaurant.dto.search.SearchFilter;
import com.example.api.review.dto.ReviewDTO;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RestaurantMapper {
    void increaseReviewCountAndRate(ReviewDTO reviewDTO);

    void decreaseReviewCountAndRate(ReviewDTO reviewDTO);

    void save(RestaurantDTO dto);

    Optional<RestaurantDTO> findById(long restaurantId);

    Optional<GetRestaurantInfoRes> findRestaurantInfoById(long restaurantId);

    boolean isAlreadyCreated(long ownerId);

    boolean isAlreadyExistsName(String name);

    boolean isAlreadyExistsNameExcludeSelf(String name, long restaurantId);

    Optional<GetRestaurantInfoRes> findRestaurantInfoByName(@Param("name") String name,
                                                            @Param("memberId") Long memberId);

    Optional<RestaurantWithHolidayAndAvailableDateDTO> findRestaurantWithHolidayAndAvailableDateById(long restaurantId);

    void updateRestaurant(RestaurantDTO dto);

    Optional<GetRestaurantInfoRes> findRestaurantInfoByOwnerId(long ownerId);

    void increaseSavedCount(RestaurantDTO restaurant);

    void decreaseSavedCount(RestaurantDTO restaurant);

    int getCountByAddress(String koreanCity);

    int getCountByCategory(String category);

    List<RestaurantSummaryDTO> searchNameByKeyword(String keyword);

    List<GetRestaurantSearchListRes> searchByFilter(SearchFilter filter, long memberId);

    // for test
    List<RestaurantDTO> findAll();
}