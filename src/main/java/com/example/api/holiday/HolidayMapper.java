package com.example.api.holiday;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HolidayMapper {

    boolean isAlreadyExistsDays(long restaurantId, @Param("holidays") List<Day> holidays);

    void saveAll(List<HolidayDTO> holidayDTOs);

    List<HolidayDTO> findAll();

    void deleteAll();

    void delete(long restaurantId);

    List<HolidayDTO> findByRestaurantId(@Param("restaurantId") long restaurantId);
}