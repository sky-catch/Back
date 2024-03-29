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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "예약", description = "예약 관련 API입니다.")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "예약 생성", description = "방문일과 방문 시간에 식당 예약을 생성하는 API입니다. 예약 생성 요청 시 결제를 생성하고 예약을 생성합니다.")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "예약 생성 성공")
    )
    public void createReservation(@Parameter(hidden = true) @LoginMember MemberDTO loginMember,
                                  @PathVariable long restaurantId,
                                  @Valid @RequestBody CreateReservationReq req) {

        CreateReservationDTO dto = CreateReservationDTO.of(restaurantId, loginMember.getMemberId(), req);

        reservationService.createReservation(dto);
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