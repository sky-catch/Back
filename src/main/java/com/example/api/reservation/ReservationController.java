package com.example.api.reservation;

import com.example.api.member.MemberDTO;
import com.example.api.reservation.dto.CreateReservationDTO;
import com.example.api.reservation.dto.GetAvailableTimeSlotDTO;
import com.example.api.reservation.dto.MyDetailReservationDTO;
import com.example.api.reservation.dto.TimeSlots;
import com.example.api.reservation.dto.request.CreateReservationReq;
import com.example.api.reservation.dto.request.GetAvailableTimeSlotsReq;
import com.example.core.exception.ExceptionResponse;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청값이 잘못된 경우 발생하는 에러입니다.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "식당이 DB에 존재하지 않는 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
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

    @GetMapping("/detail/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "예약 상세 조회", description = "예약의 상세 내용을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "로그인한 회원의 예약이 아닌 경우 발생 또는 요청값이 잘못된 경우 발생하는 에러입니다.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "예약이 DB에 존재하지 않는 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public MyDetailReservationDTO getMyDetailReservationById(
            @Parameter(hidden = true) @LoginMember MemberDTO loginMember,
            @PathVariable long reservationId) {

        return reservationService.getMyDetailReservationById(reservationId, loginMember.getMemberId());
    }

    @PatchMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "예약 취소", description = "예약 ID로 내 예약을 취소하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "400", description = "로그인한 회원의 예약이 아닌 경우 발생 또는 예약 상태가 PLANNED가 아닌 경우 발생 또는 요청값이 잘못된 경우 발생하는 에러입니다.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "예약이 DB에 존재하지 않는 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "502", description = "아임포트에서 결제를 찾을 수 없거나 삭제할 수 없는 에러 또는 결제 미완료, 결제 금액 위변조 의심 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public void cancelReservation(@Parameter(hidden = true) @LoginMember MemberDTO loginMember,
                                  @PathVariable long reservationId) {

        reservationService.cancelMyReservationById(reservationId, loginMember.getMemberId());
    }
}