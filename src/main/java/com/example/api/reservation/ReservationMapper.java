package com.example.api.reservation;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.owner.dto.ReservationCount;
import com.example.api.owner.dto.ReservationOfRestaurant;
import com.example.api.reservation.dto.MyReservationDTO;
import com.example.api.reservation.dto.ReservationWithRestaurantAndPaymentDTO;
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

    void updateStatusById(@Param("reservationId") long reservationId, @Param("status") ReservationStatus status);

    Optional<ReservationWithRestaurantAndPaymentDTO> findMyDetailReservationById(long reservationId);

    List<ReservationWithRestaurantAndPaymentDTO> findDetailByIds(@Param("noShowIds") List<Long> noShowIds);

    void bulkUpdateStatusByIds(@Param("noShowIds") List<Long> noShowIds, @Param("status") ReservationStatus status);

    List<ReservationDTO> findByCond(ReservationSearchCond cond);

    // for test
    List<ReservationDTO> findAll();

    List<ReservationOfRestaurant> getReservationOfRestaurant(long ownerId);

    List<ReservationCount> getReservationCount(long ownerId);
}