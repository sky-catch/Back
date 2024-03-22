package com.example.api.reservationavailabledate;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationAvailableDateMapper {
    void save(ReservationAvailableDateDTO dto);

    List<ReservationAvailableDateDTO> findAll();

    void deleteAll();
}