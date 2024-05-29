package com.example.api.reservation;

import com.example.api.alarm.AlarmService;
import com.example.api.mydining.GetMyReservationDTO;
import com.example.api.owner.dto.GetReservationOfRestaurantRes;
import com.example.api.owner.dto.ReservationCount;
import com.example.api.owner.dto.ReservationOfRestaurant;
import com.example.api.payment.PaymentMapper;
import com.example.api.payment.domain.PaymentDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.MyReservationDTO;
import com.example.api.reservation.dto.ReservationWithRestaurantAndPaymentDTO;
import com.example.api.reservation.dto.TimeSlot;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.condition.ReservationSearchCond;
import com.example.api.reservation.dto.request.ChangeReservationsStatusToNoShowReq;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.RestaurantWithAvailableDateDTO;
import com.example.api.restaurant.exception.RestaurantExceptionType;
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
import org.springframework.dao.DuplicateKeyException;
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
    public void createReservation(CreateReservationDTO dto) {
        log.info("예약 생성");

        RestaurantWithAvailableDateDTO restaurantWithAvailableDate = restaurantMapper.findRestaurantWithAvailableDateById(
                dto.getRestaurantId()).orElseThrow(() -> new SystemException(RestaurantExceptionType.NOT_FOUND));
        validate(dto, restaurantWithAvailableDate);

        ReservationDTO reservation = dto.toReservationDTO();
        if (dto.isShouldPay()) {
            PaymentDTO paymentReady = PaymentDTO.ofReadyStatus(dto.getAmountToPay());
            paymentMapper.save(paymentReady);

            reservation.setPaymentId(paymentReady.getPaymentId());
        }

        saveIfNotDuplicated(reservation);

        alarmService.createReservationAlarm(reservation.getReservationId(), reservation.getReservationDateTime());
    }

    private void saveIfNotDuplicated(ReservationDTO reservation) {
        try {
            reservationMapper.save(reservation);
        } catch (DuplicateKeyException e) {
            log.error("식당 ID {}는 {}에 이미 예약이 존재합니다.", reservation.getRestaurantId(),
                    reservation.getReservationDateTime());
            throw new SystemException(ReservationExceptionType.TIME_DUPLICATE);
        }
    }

    private void validate(CreateReservationDTO dto, RestaurantWithAvailableDateDTO restaurantWithAvailableDate) {
        validateStatus(dto);
        validatePerson(dto, restaurantWithAvailableDate);
        validateVisitTime(dto.getVisitTime(), restaurantWithAvailableDate);
        validateAvailableDate(dto, restaurantWithAvailableDate);
        validateMinutes(dto);
    }

    private void validateStatus(CreateReservationDTO dto) {
        if (dto.getStatus() != ReservationStatus.PLANNED) {
            log.error("예약 상태 요청 값 {}는 {}가 아닙니다.", dto.getStatus(), ReservationStatus.PLANNED);
            throw new SystemException(ReservationExceptionType.NOT_VALID_STATUS);
        }
    }

    private void validatePerson(CreateReservationDTO dto, RestaurantWithAvailableDateDTO restaurantWithAvailableDate) {
        if (restaurantWithAvailableDate.isOutboundTablePerson(dto.getNumberOfPeople())) {
            log.error("{} 해당 예약 인원은 {} 식당의 {} ~ {} 최소, 최대 인원수에 맞지 않습니다.", dto.getNumberOfPeople(),
                    restaurantWithAvailableDate.getName(),
                    restaurantWithAvailableDate.getTablePersonMin(),
                    restaurantWithAvailableDate.getTablePersonMax());
            throw new SystemException(ReservationExceptionType.OUTBOUND_PERSON);
        }
    }

    private void validateVisitTime(LocalTime visitTime, RestaurantWithAvailableDateDTO restaurantWithAvailableDate) {
        if (restaurantWithAvailableDate.isNotValidVisitTime(visitTime)) {
            log.error("예약 시간 {}는 {} 식당의 오픈 시간 {} ~ 주문 마감 시간 {} 사이가 아닙니다.", visitTime,
                    restaurantWithAvailableDate.getName(),
                    restaurantWithAvailableDate.getOpenTime(),
                    restaurantWithAvailableDate.getLastOrderTime());
            throw new SystemException(ReservationExceptionType.NOT_VALID_VISIT_TIME);
        }
    }

    private void validateAvailableDate(CreateReservationDTO dto,
                                       RestaurantWithAvailableDateDTO restaurantWithAvailableDate) {
        if (restaurantWithAvailableDate.isNotAvailableDate(dto.getVisitDate())) {
            log.error("{}는 {} 식당의 예약 가능한 날짜가 아닙니다.", dto.getVisitDate(),
                    restaurantWithAvailableDate.getAvailableDate());
            throw new SystemException(ReservationExceptionType.NOT_AVAILABLE_DATE);
        }
    }

    private void validateMinutes(CreateReservationDTO dto) {
        if (!dto.isValidMinute()) {
            log.error("예약 시간 {}는 00분 또는 30분 단위가 아닙니다.", dto.getTime());
            throw new SystemException(ReservationExceptionType.NOT_VALID_MINUTES);
        }
    }

    @Transactional(readOnly = true)
    public TimeSlots getAvailableTimeSlots(GetAvailableTimeSlotDTO dto) {
        RestaurantWithAvailableDateDTO restaurantWithAvailableDate = restaurantMapper.findRestaurantWithAvailableDateById(
                dto.getRestaurantId()).orElseThrow(() -> new SystemException(ReservationExceptionType.NOT_FOUND));

        validateVisitTime(dto.getVisitTime(), restaurantWithAvailableDate);

        if (restaurantWithAvailableDate.isOutboundTablePerson(dto.getNumberOfPeople())) {
            return TimeSlots.of(new ArrayList<>());
        }

        TimeSlots allTimeSlots = getAllTimeSlots(dto.getVisitTime(), restaurantWithAvailableDate.getLastOrderTime());
        TimeSlots reservationTimeSlots = getReservationTimeSlots(dto);
        return allTimeSlots.subtract(reservationTimeSlots);
    }

    private TimeSlots getAllTimeSlots(LocalTime visitTime, LocalTime lastOrderTime) {
        List<TimeSlot> allTimeSlots = new ArrayList<>();
        TimeSlot timeSlot = TimeSlot.of(visitTime);
        while (timeSlot.isBeforeOrEqual(lastOrderTime)) {
            allTimeSlots.add(timeSlot);
            timeSlot = timeSlot.getNextTimeSlot();
        }
        return TimeSlots.of(allTimeSlots);
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
            log.error("주문의 memberId = {}, 로그인 유저의 memberId = {}가 달라서 취소할 수 없습니다.", reservation.getMemberId(), memberId);
            throw new SystemException(ReservationExceptionType.NOT_MINE);
        }
        if (reservation.isNotPlanned()) {
            log.error("주문의 status = {}가 planned가 아니라서 취소할 수 없습니다.", reservation.getStatus());
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

    public GetReservationOfRestaurantRes getReservationOfRestaurant(long ownerId) {
        List<ReservationOfRestaurant> reservationOfRestaurant = reservationMapper.getReservationOfRestaurant(ownerId);
        List<ReservationCount> reservationCount = reservationMapper.getReservationCount(ownerId);

        return new GetReservationOfRestaurantRes(reservationOfRestaurant, reservationCount);
    }
}