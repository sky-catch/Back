package com.example.api.reservation;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.GetReservationRes;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {

    List<GetReservationRes> getMyReservationsByStatus(GetMyReservationDTO dto);

    void save(ReservationDTO dto);

    void deleteAll();
}