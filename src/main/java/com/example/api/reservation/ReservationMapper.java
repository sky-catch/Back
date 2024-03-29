package com.example.api.reservation;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.condition.DuplicateReservationSearchCond;
import com.example.api.reservation.dto.condition.ReservationSearchCond;
import com.example.api.reservation.dto.response.GetReservationRes;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {

    List<GetReservationRes> getMyReservationsByStatus(GetMyReservationDTO dto);

    void save(ReservationDTO dto);

    void deleteAll();

    ReservationDTO getReservation(long reservationId);

    List<ReservationDTO> findByRestaurantIdAndStatusAndSearchDateAndGreaterThanOrEqualToVisitTime(
            ReservationSearchCond cond);

    boolean isAlreadyExistsByRestaurantIdAndMemberIdAndTime(DuplicateReservationSearchCond cond);
}