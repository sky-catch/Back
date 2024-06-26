package com.example.api.restaurant;

import com.example.api.restaurant.dto.GetAllRestaurant;
import com.example.api.restaurant.dto.GetRestaurantInfoRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantWithAvailableDateDTO;
import com.example.api.restaurant.dto.enums.HotPlace;
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
    void increaseReviewCountAndRate(long restaurantId, int rate);

    void decreaseReviewCountAndRate(ReviewDTO reviewDTO);

    void updateReviewRate(int oldReviewRate, int newReviewRate, long restaurantId);

    void save(RestaurantDTO dto);

    Optional<RestaurantDTO> findById(long restaurantId);

    Optional<GetRestaurantInfoRes> findRestaurantInfoById(long restaurantId);

    boolean isAlreadyCreated(long ownerId);

    boolean isAlreadyExistsName(String name);

    boolean isAlreadyExistsNameExcludeSelf(String name, long ownerId);

    Optional<GetRestaurantInfoRes> findRestaurantInfoByName(@Param("name") String name,
                                                            @Param("memberId") Long memberId);

    Optional<RestaurantWithAvailableDateDTO> findRestaurantWithAvailableDateById(long restaurantId);

    void updateRestaurant(RestaurantDTO dto);

    Optional<GetRestaurantInfoRes> findRestaurantInfoByOwnerId(long ownerId);

    Optional<RestaurantDTO> findByOwnerId(long ownerId);

    void increaseSavedCount(RestaurantDTO restaurant);

    void decreaseSavedCount(RestaurantDTO restaurant);

    int getCountByAddress(String koreanCity);

    int getCountByCategory(String category);

    int getCountByHotPlace(String hotPlace);

    List<RestaurantSummaryDTO> searchNameByKeyword(String keyword);

    List<GetRestaurantSearchListRes> searchByFilter(@Param("filter") SearchFilter filter,
                                                    @Param("list") List<HotPlace> list);

    // for test
    List<RestaurantDTO> findAll();

    List<GetAllRestaurant> getAllRestaurant(long memberId);

}