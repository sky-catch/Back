package com.example.api.reservation;

import com.example.api.member.MemberDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.request.CreateReservationReq;
import com.example.api.reservation.dto.request.GetAvailableTimeSlotsReq;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
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

    @PostMapping("/{restaurantId}")
    @Operation(summary = "예약 생성", description = "방문일과 방문 시간에 식당 예약하는 API입니다.")
    public ResponseEntity<Void> createReservation(@Parameter(hidden = true) @LoginMember MemberDTO loginMember,
                                                  @PathVariable long restaurantId,
                                                  @Valid @RequestBody CreateReservationReq req) {

        // todo paymentId 수정하기
        CreateReservationDTO dto = CreateReservationDTO.reqToPlannedReservationDTO(restaurantId,
                loginMember.getMemberId(), req);

        long reservationId = reservationService.createReservation(dto);

        URI uri = URI.create("/reservations/" + restaurantId + "/" + reservationId);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/availTimeSlots")
    @Operation(summary = "예약 가능한 시간 조회", description = "방문일에 예약 가능한 시간들을 조회하는 API입니다.")
    public ResponseEntity<TimeSlots> getAvailableTimeSlots(@Valid @RequestBody GetAvailableTimeSlotsReq req) {
        GetAvailableTimeSlotDTO dto = GetAvailableTimeSlotDTO.builder()
                .restaurantId(req.getRestaurantId())
                .numberOfPeople(req.getNumberOfPeople())
                .searchDate(req.getSearchDate())
                .visitTime(req.getVisitTime())
                .build();

        TimeSlots availableTimeSlots = reservationService.getAvailableTimeSlots(dto);

        return ResponseEntity.ok(availableTimeSlots);
    }
}