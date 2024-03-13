package com.example.api.reservation;

import com.example.api.member.MemberDTO;
import com.example.api.reservation.dto.CreateReservationReq;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.request.GetAvailableTimeSlotsReq;
import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.ArrayList;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "예약", description = "예약 관련 API입니다.")
public class ReservationController {

    private final ReservationService reservationService;
    private final RestaurantService restaurantService;

    @PostMapping("/{restaurantId}")
    public ResponseEntity<Void> createReservation(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO,
                                                  @PathVariable long restaurantId,
                                                  @RequestBody CreateReservationReq req) {

        // todo reservationDayId, paymentId 수정하기
        ReservationDTO dto = ReservationDTO.builder()
                .restaurantId(restaurantId)
                .memberId(memberDTO.getMemberId())
                .reservationDayId(1L)
                .paymentId(1L)
                .time(req.getTime())
                .numberOfPeople(req.getNumberOfPeople())
                .memo(req.getMemo())
                .status(ReservationStatus.PLANNED)
                .build();

        long reservationId = reservationService.createReservation(dto);

        URI uri = URI.create("/reservations/" + restaurantId + "/" + reservationId);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/availTimeSlots")
    @Operation(summary = "예약 가능한 시간 조회", description = "해당 날짜에 예약 가능한 시간들을 조회하는 API입니다.")
    public ResponseEntity<TimeSlots> getAvailableTimeSlots(@Valid @RequestBody GetAvailableTimeSlotsReq req) {

        RestaurantDTO restaurant = restaurantService.getRestaurantById(req.getRestaurantId());

        if (restaurant.checkNumberOfPeople(req.getNumberOfPeople())) {
            return ResponseEntity.ok(new TimeSlots(new ArrayList<>()));
        }

        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantDTO(restaurant)
                .numberOfPeople(req.getNumberOfPeople())
                .searchDate(req.getSearchDate())
                .visitTime(req.getVisitTime())
                .build();

        TimeSlots availableTimeSlots = reservationService.getAvailableTimeSlots(dto);

        return ResponseEntity.ok(availableTimeSlots);
    }
}