package com.example.api.holiday;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HolidayMapper {

    boolean isAlreadyExistsDays(long restaurantId, @Param("holidays") Holidays holidays);

    void saveAll(@Param("restaurantId") long restaurantId, @Param("holidays") Holidays holidays);

    List<HolidayDTO> findAll();

    void delete(long restaurantId);

    List<HolidayDTO> findByRestaurantId(@Param("restaurantId") long restaurantId);
}