package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.PLANNED;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.GetReservationRes;
import com.example.api.reservation.dto.ReservationSearchCond;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantWithHolidayDTO;
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
        RestaurantWithHolidayDTO restaurantWithHoliday = restaurantService.getRestaurantWithHolidayById(
                dto.getRestaurantId());

        if (restaurantWithHoliday.isNotValidVisitTime(dto.getVisitTime())) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_VISIT_TIME.getMessage());
        }
        if (restaurantWithHoliday.isHoliday(dto.getSearchDate())) {
            return TimeSlots.of(new ArrayList<>());
        }
        if (restaurantWithHoliday.isOutboundTablePerson(dto.getNumberOfPeople())) {
            return TimeSlots.of(new ArrayList<>());
        }

        TimeSlots allAvailableTimeSlotsFromVisitTimeToLastOrderTime = createAllAvailableTimeSlotsFromVisitTimeToLastOrderTime(
                dto.getVisitTime(), restaurantWithHoliday.getLastOrderTime());
        TimeSlots reservationTimeSlots = getReservationTimeSlots(dto);
        return allAvailableTimeSlotsFromVisitTimeToLastOrderTime.subtract(reservationTimeSlots);
    }

    private TimeSlots createAllAvailableTimeSlotsFromVisitTimeToLastOrderTime(LocalTime visitTime,
                                                                              LocalTime lastOrderTime) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        TimeSlot availableTimeSlot = TimeSlot.of(visitTime);
        while (availableTimeSlot.isBeforeOrEqual(lastOrderTime)) {
            availableTimeSlots.add(availableTimeSlot);
            availableTimeSlot = availableTimeSlot.getNextTimeSlot();
        }
        return TimeSlots.of(availableTimeSlots);
    }

    private TimeSlots getReservationTimeSlots(GetAvailableTimeSlotDTO dto) {
        ReservationSearchCond cond = ReservationSearchCond.builder()
                .restaurantId(dto.getRestaurantId())
                .searchDate(dto.getSearchDate())
                .visitTime(dto.getVisitTime())
                .status(PLANNED)
                .build();
        List<ReservationDTO> findReservations = reservationMapper.findByRestaurantIdAndStatusAndSearchDateAndGreaterThanOrEqualToVisitTime(
                cond);

        List<TimeSlot> reservationTimeSlots = findReservations.stream()
                .map(ReservationDTO::getTime)
                .map(LocalDateTime::toLocalTime)
                .map(TimeSlot::of)
                .collect(Collectors.toList());
        return TimeSlots.of(reservationTimeSlots);
    }
}