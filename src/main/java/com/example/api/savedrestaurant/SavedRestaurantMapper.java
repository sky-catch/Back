package com.example.api.savedrestaurant;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SavedRestaurantMapper {

    boolean isAlreadyExistsByRestaurantIdAndMemberId(long restaurantId, long memberId);

    void save(SavedRestaurantDTO savedRestaurant);

    void delete(SavedRestaurantDTO savedRestaurant);

    List<SavedRestaurantDTO> findAll();

    void deleteAll();
}