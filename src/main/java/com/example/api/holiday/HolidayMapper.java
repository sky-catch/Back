package com.example.api.holiday;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HolidayMapper {

    boolean isAlreadyExistsDays(long restaurantId, List<Day> days);

    void saveAll(List<HolidayDTO> holidayDTOs);

    List<HolidayDTO> findAll();

    void deleteAll();

    void delete(long restaurantId);
}