package com.example.api.reservationavailabledate;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationAvailableDateMapper {
    void save(ReservationAvailableDateDTO dto);

    void update(ReservationAvailableDateDTO dto);

    List<ReservationAvailableDateDTO> findAll();

    ReservationAvailableDateDTO findByRestaurantId(@Param("restaurantId") long restaurantId);
}