package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.PLANNED;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.condition.ReservationSearchCond;
import com.example.api.reservation.dto.response.GetReservationRes;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantService;
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

    @Transactional(readOnly = true)
    public List<GetReservationRes> getMyReservations(GetMyReservationDTO dto) {
        return reservationMapper.getMyReservationsByStatus(dto);
    }

    @Transactional
    public long createReservation(CreateReservationDTO dto) {
        // 식당 존재 유무
        RestaurantWithHolidayDTO restaurantWithHoliday = restaurantService.getRestaurantWithHolidayById(
                dto.getRestaurantId());

        // 예약 상태
        if (dto.getStatus() != PLANNED) {
            throw new SystemException("잘못된 예약 상태입니다.");
        }
        // 인원
        if (restaurantWithHoliday.isOutboundTablePerson(dto.getNumberOfPeople())) {
            throw new SystemException("방문 인원 수가 잘못됐습니다.");
        }
        // 예약 날짜
        if (restaurantWithHoliday.isHoliday(dto.getVisitDate())) {
            throw new SystemException("휴일에는 예약할 수 없습니다.");
        }
        // 시간
        if (restaurantWithHoliday.isNotValidVisitTime(dto.getVisitTime())) {
            throw new SystemException("방문 시간이 잘못됐습니다.");
        }

        ReservationDTO reservation = dto.toReservationDTO();

        reservationMapper.save(reservation);

        return reservation.getReservationId();
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