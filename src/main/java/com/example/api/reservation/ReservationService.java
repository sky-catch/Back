package com.example.api.reservation;

import com.example.api.alarm.AlarmService;
import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.payment.PaymentMapper;
import com.example.api.payment.domain.PaymentDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.MyReservationDTO;
import com.example.api.reservation.dto.ReservationWithRestaurantAndPaymentDTO;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.condition.DuplicateReservationSearchCond;
import com.example.api.reservation.dto.condition.ReservationSearchCond;
import com.example.api.reservation.dto.request.ChangeReservationsStatusToNoShowReq;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantWithHolidayAndAvailableDateDTO;
import com.example.core.exception.SystemException;
import com.example.core.payment.CorePaymentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final RestaurantMapper restaurantMapper;
    private final PaymentMapper paymentMapper;
    private final AlarmService alarmService;
    private final CorePaymentService corePaymentService;

    @Transactional(readOnly = true)
    public List<MyReservationDTO> getMyReservations(GetMyReservationDTO dto) {
        return reservationMapper.getMyReservationsByStatus(dto);
    }

    @Transactional
    public synchronized void createReservation(CreateReservationDTO dto) {
        log.info("주문 생성");

        RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate = restaurantMapper.findRestaurantWithHolidayAndAvailableDateById(
                dto.getRestaurantId()).orElseThrow(() -> new SystemException(ReservationExceptionType.NOT_FOUND));
        validate(dto, restaurantWithHolidayAndAvailableDate);

        PaymentDTO paymentReady = PaymentDTO.ofReadyStatus(dto.getAmountToPay());
        paymentMapper.save(paymentReady);

        ReservationDTO reservation = dto.toReservationDTO();
        reservation.setPaymentId(paymentReady.getPaymentId());
        reservationMapper.save(reservation);

        alarmService.createReservationAlarm(reservation.getReservationId(), reservation.getReservationDateTime());
    }

    private void validate(CreateReservationDTO dto,
                          RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        validateStatus(dto);
        validatePerson(dto, restaurantWithHolidayAndAvailableDate);
        validateHoliday(dto, restaurantWithHolidayAndAvailableDate);
        validateVisitTime(dto, restaurantWithHolidayAndAvailableDate);
        validateAvailableDate(dto, restaurantWithHolidayAndAvailableDate);
        validateMinutes(dto);
        validateDuplicate(dto);
    }

    private void validateStatus(CreateReservationDTO dto) {
        if (dto.getStatus() != ReservationStatus.PLANNED) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_STATUS);
        }
    }

    private void validatePerson(CreateReservationDTO dto,
                                RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isOutboundTablePerson(dto.getNumberOfPeople())) {
            throw new SystemException(ReservationExceptionType.OUTBOUND_PERSON);
        }
    }

    private void validateHoliday(CreateReservationDTO dto,
                                 RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isHoliday(dto.getVisitDate())) {
            throw new SystemException(ReservationExceptionType.RESERVATION_ON_HOLIDAY);
        }
    }

    private void validateVisitTime(CreateReservationDTO dto,
                                   RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isNotValidVisitTime(dto.getVisitTime())) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_VISIT_TIME);
        }
    }

    private void validateAvailableDate(CreateReservationDTO dto,
                                       RestaurantWithHolidayAndAvailableDateDTO restaurantWithHolidayAndAvailableDate) {
        if (restaurantWithHolidayAndAvailableDate.isNotAvailableDate(dto.getVisitDate())) {
            throw new SystemException(ReservationExceptionType.NOT_AVAILABLE_DATE);
        }
    }

    private void validateMinutes(CreateReservationDTO dto) {
        int minute = dto.getTime().getMinute();
        if (!(minute == 0 || minute == 30)) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_MINUTES);
        }
    }

    private void validateDuplicate(CreateReservationDTO dto) {
        DuplicateReservationSearchCond cond = DuplicateReservationSearchCond.builder()
                .restaurantId(dto.getRestaurantId())
                .memberId(dto.getMemberId())
                .time(LocalDateTime.of(dto.getVisitDate(), dto.getVisitTime()))
                .build();
        if (reservationMapper.isAlreadyExistsByRestaurantIdAndMemberIdAndTime(cond)) {
            throw new SystemException(ReservationExceptionType.ALREADY_EXISTS_AT_TIME);
        }
    }

    @Transactional(readOnly = true)
    public TimeSlots getAvailableTimeSlots(GetAvailableTimeSlotDTO dto) {
        RestaurantWithHolidayAndAvailableDateDTO restaurantWithHoliday = restaurantMapper.findRestaurantWithHolidayAndAvailableDateById(
                dto.getRestaurantId()).orElseThrow(() -> new SystemException(ReservationExceptionType.NOT_FOUND));

        if (restaurantWithHoliday.isNotValidVisitTime(dto.getVisitTime())) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_VISIT_TIME);
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
                .status(ReservationStatus.PLANNED)
                .build();
        List<ReservationDTO> findReservations = reservationMapper.findByRestaurantIdAndStatusAndSearchDateAndGreaterThanOrEqualToVisitTime(
                cond);

        List<TimeSlot> reservationTimeSlots = findReservations.stream()
                .map(ReservationDTO::getReservationDateTime)
                .map(LocalDateTime::toLocalTime)
                .map(TimeSlot::of)
                .collect(Collectors.toList());
        return TimeSlots.of(reservationTimeSlots);
    }

    @Transactional(readOnly = true)
    public ReservationWithRestaurantAndPaymentDTO getMyDetailReservationById(long reservationId, long memberId) {
        ReservationWithRestaurantAndPaymentDTO reservation = reservationMapper.findMyDetailReservationById(
                        reservationId)
                .orElseThrow(() -> new SystemException(ReservationExceptionType.NOT_FOUND));
        if (reservation.isNotMine(memberId)) {
            throw new SystemException(ReservationExceptionType.NOT_MINE);
        }

        return reservation;
    }

    @Transactional
    public void cancelMyReservationById(long reservationId, long memberId) {
        log.info("내 주문 취소");

        ReservationWithRestaurantAndPaymentDTO reservation = reservationMapper.findMyDetailReservationById(
                        reservationId)
                .orElseThrow(() -> new SystemException(ReservationExceptionType.NOT_FOUND));
        if (reservation.isNotMine(memberId)) {
            throw new SystemException(ReservationExceptionType.NOT_MINE);
        }
        if (reservation.isNotPlanned()) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_STATUS);
        }

        PaymentDTO payment = reservation.getPayment();
        corePaymentService.cancelPaymentByImpUid(payment.getImpUid(), payment.getPrice());

        reservationMapper.updateStatusById(reservationId, ReservationStatus.CANCEL);
    }

    @Transactional
    public void changeReservationsToNoShowByRequest(ChangeReservationsStatusToNoShowReq req) {
        List<ReservationWithRestaurantAndPaymentDTO> reservations = reservationMapper.findDetailByIds(
                req.getNoShowIds());

        boolean notValidStatus = reservations.stream()
                .map(ReservationWithRestaurantAndPaymentDTO::getStatus)
                .anyMatch(reservationStatus -> reservationStatus != ReservationStatus.PLANNED);
        if (notValidStatus) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_STATUS);
        }

        reservationMapper.bulkUpdateStatusByIds(req.getNoShowIds(), ReservationStatus.CANCEL);
    }

    @Transactional
    public List<Long> changeReservationsToNoShowByDate(LocalDate date) {
        ReservationSearchCond cond = ReservationSearchCond.builder()
                .searchDate(date)
                .status(ReservationStatus.PLANNED)
                .build();
        List<Long> noShowIds = reservationMapper.findByCond(cond).stream()
                .map(ReservationDTO::getReservationId)
                .collect(Collectors.toList());

        reservationMapper.bulkUpdateStatusByIds(noShowIds, ReservationStatus.CANCEL);

        return noShowIds;
    }
}