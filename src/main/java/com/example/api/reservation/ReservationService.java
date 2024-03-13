package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.PLANNED;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.FindAvailableTimeSlotDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.GetReservationRes;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final RestaurantService restaurantService;

    public List<GetReservationRes> getMyReservations(GetMyReservationDTO dto) {
        return reservationMapper.getMyReservationsByStatus(dto);
    }

    public long createReservation(ReservationDTO dto) {
        // 1. 식당 존재 유무
        RestaurantDTO restaurant = restaurantService.getRestaurantById(dto.getRestaurantId());

        // 2. 예약 상태
        if (dto.getStatus() != PLANNED) {
            throw new SystemException("잘못된 예약 상태입니다.");
        }

        // 3. 예약 날짜, 시간, 인원
        restaurant.checkCanMakeReservation(dto);

        reservationMapper.save(dto);

        return dto.getReservationId();
    }

    public TimeSlots getAvailableTimeSlots(GetAvailableTimeSlotDTO dto) {
        FindAvailableTimeSlotDTO findAvailableTimeSlotDTO = FindAvailableTimeSlotDTO.builder()
                .restaurantId(dto.getRestaurantId())
                .searchDate(dto.getSearchDate())
                .visitTime(dto.getVisitTime())
                .status(PLANNED)
                .build();

        RestaurantDTO restaurantDTO = dto.getRestaurantDTO();
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        LocalTime openTime = LocalTime.parse(restaurantDTO.getOpenTime());
        LocalTime lastOrderTime = LocalTime.parse(restaurantDTO.getLastOrderTime());
        for (TimeSlot availableTimeSlot = TimeSlot.of(openTime); availableTimeSlot.isBeforeOrEqual(lastOrderTime);
             availableTimeSlot = availableTimeSlot.getNextTimeSlot()) {
            availableTimeSlots.add(availableTimeSlot);
        }

        List<ReservationDTO> reservationDTOSByRestaurantIdAndSearchDateAndGreaterThanOrEqualToVisitTime = reservationMapper.findByRestaurantIdAndSearchDateAndGreaterThanOrEqualToVisitTime(
                findAvailableTimeSlotDTO);
        for (ReservationDTO reservationDTO : reservationDTOSByRestaurantIdAndSearchDateAndGreaterThanOrEqualToVisitTime) {
            LocalTime reservationTime = reservationDTO.getTime().toLocalTime();
            TimeSlot reservationTimeSlot = TimeSlot.of(reservationTime);
            availableTimeSlots.remove(reservationTimeSlot);
        }

        return TimeSlots.of(availableTimeSlots);
    }
}