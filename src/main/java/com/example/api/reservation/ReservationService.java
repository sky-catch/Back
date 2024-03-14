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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final RestaurantService restaurantService;

    public List<GetReservationRes> getMyReservations(GetMyReservationDTO dto) {
        return reservationMapper.getMyReservationsByStatus(dto);
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public TimeSlots getAvailableTimeSlots(GetAvailableTimeSlotDTO dto) {
        RestaurantDTO restaurantDTO = dto.getRestaurantDTO();
        List<TimeSlot> allAvailableTimeSlots = createAllAvailableTimeSlots(restaurantDTO);

        FindAvailableTimeSlotDTO findAvailableTimeSlotDTO = FindAvailableTimeSlotDTO.builder()
                .restaurantId(dto.getRestaurantId())
                .searchDate(dto.getSearchDate())
                .visitTime(dto.getVisitTime())
                .status(PLANNED)
                .build();
        List<TimeSlot> availableTimeSlots = subtractReservationTimeSlots(findAvailableTimeSlotDTO,
                allAvailableTimeSlots);

        return TimeSlots.of(availableTimeSlots);
    }

    private List<TimeSlot> createAllAvailableTimeSlots(RestaurantDTO restaurantDTO) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        LocalTime openTime = LocalTime.parse(restaurantDTO.getOpenTime());
        LocalTime lastOrderTime = LocalTime.parse(restaurantDTO.getLastOrderTime());
        for (TimeSlot availableTimeSlot = TimeSlot.of(openTime); availableTimeSlot.isBeforeOrEqual(lastOrderTime);
             availableTimeSlot = availableTimeSlot.getNextTimeSlot()) {
            availableTimeSlots.add(availableTimeSlot);
        }
        return availableTimeSlots;
    }

    private List<TimeSlot> subtractReservationTimeSlots(FindAvailableTimeSlotDTO findAvailableTimeSlotDTO,
                                                        List<TimeSlot> allAvailableTimeSlots) {
        List<ReservationDTO> reservationDTOSByRestaurantIdAndSearchDateAndGreaterThanOrEqualToVisitTime = reservationMapper.findByRestaurantIdAndSearchDateAndGreaterThanOrEqualToVisitTime(
                findAvailableTimeSlotDTO);
        List<TimeSlot> reservationTimeSlots = reservationDTOSByRestaurantIdAndSearchDateAndGreaterThanOrEqualToVisitTime.stream()
                .map(ReservationDTO::getTime)
                .map(LocalDateTime::toLocalTime)
                .map(TimeSlot::of)
                .collect(Collectors.toList());
        allAvailableTimeSlots.removeAll(reservationTimeSlots);

        return allAvailableTimeSlots;
    }
}