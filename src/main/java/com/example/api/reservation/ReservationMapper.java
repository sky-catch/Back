package com.example.api.reservation;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.MyDetailReservationDTO;
import com.example.api.reservation.dto.MyReservationDTO;
import com.example.api.reservation.dto.condition.DuplicateReservationSearchCond;
import com.example.api.reservation.dto.condition.ReservationSearchCond;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationMapper {

    List<MyReservationDTO> getMyReservationsByStatus(GetMyReservationDTO dto);

    void save(ReservationDTO dto);

    Optional<ReservationDTO> getReservation(long reservationId);

    List<ReservationDTO> findByRestaurantIdAndStatusAndSearchDateAndGreaterThanOrEqualToVisitTime(
            ReservationSearchCond cond);

    boolean isAlreadyExistsByRestaurantIdAndMemberIdAndTime(DuplicateReservationSearchCond cond);

    void updateStatusById(@Param("reservationId") long reservationId, @Param("status") ReservationStatus status);

    List<ReservationDTO> findAll();

    void deleteAll();

    Optional<MyDetailReservationDTO> findMyDetailReservationById(long reservationId);
}