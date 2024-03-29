package com.example.api.reservation;

import static com.example.api.reservation.ReservationStatus.PLANNED;

import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.payment.PaymentMapper;
import com.example.api.payment.domain.PaymentDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.condition.DuplicateReservationSearchCond;
import com.example.api.reservation.dto.condition.ReservationSearchCond;
import com.example.api.reservation.dto.response.GetReservationRes;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantWithHolidayAndAvailableDateDTO;
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
    // todo restaurantService -> restaurantMapper로 변경하기
    private final RestaurantService restaurantService;

    private final PaymentMapper paymentMapper;

    @Transactional(readOnly = true)
    public List<GetReservationRes> getMyReservations(GetMyReservationDTO dto) {
        return reservationMapper.getMyReservationsByStatus(dto);
    }

    @Transactional
    public void createReservation(CreateReservationDTO dto) {
        RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate = restaurantService.getRestaurantWithHolidayAndAvailableDateById(
                dto.getRestaurantId());

        validateStatus(dto);
        validatePerson(dto, restaurantWithHolidayAndAvailableDate);
        validateHoliday(dto, restaurantWithHolidayAndAvailableDate);
        validateVisitTime(dto, restaurantWithHolidayAndAvailableDate);
        validateAvailableDate(dto, restaurantWithHolidayAndAvailableDate);
        validateDuplicate(dto);

        PaymentDTO paymentReady = PaymentDTO.ofReadyStatus(dto.getAmountToPay());
        paymentMapper.save(paymentReady);

        ReservationDTO reservation = dto.toReservationDTO();
        reservation.setPaymentId(paymentReady.getPaymentId());
        reservationMapper.save(reservation);
    }

    private void validateStatus(CreateReservationDTO dto) {
        if (dto.getStatus() != PLANNED) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_STATUS.getMessage());
        }
    }

    private void validatePerson(CreateReservationDTO dto,
                                RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isOutboundTablePerson(dto.getNumberOfPeople())) {
            throw new SystemException(ReservationExceptionType.OUTBOUND_PERSON.getMessage());
        }
    }

    private void validateHoliday(CreateReservationDTO dto,
                                 RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isHoliday(dto.getVisitDate())) {
            throw new SystemException(ReservationExceptionType.RESERVATION_ON_HOLIDAY.getMessage());
        }
    }

    private void validateVisitTime(CreateReservationDTO dto,
                                   RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isNotValidVisitTime(dto.getVisitTime())) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_VISIT_TIME.getMessage());
        }
    }

    private void validateAvailableDate(CreateReservationDTO dto,
                                       RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isNotAvailableDate(dto.getVisitDate())) {
            throw new SystemException(ReservationExceptionType.NOT_AVAILABLE_DATE.getMessage());
        }
    }

    private void validateDuplicate(CreateReservationDTO dto) {
        DuplicateReservationSearchCond cond = DuplicateReservationSearchCond.builder()
                .restaurantId(dto.getRestaurantId())
                .memberId(dto.getMemberId())
                .time(LocalDateTime.of(dto.getVisitDate(), dto.getVisitTime()))
                .build();
        if (reservationMapper.isAlreadyExistsByRestaurantIdAndMemberIdAndTime(cond)) {
            throw new SystemException(ReservationExceptionType.ALREADY_EXISTS_AT_TIME.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public TimeSlots getAvailableTimeSlots(GetAvailableTimeSlotDTO dto) {
        RestaurantWithHolidayAndAvailableDateDTO restaurantWithHoliday = restaurantService.getRestaurantWithHolidayAndAvailableDateById(
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