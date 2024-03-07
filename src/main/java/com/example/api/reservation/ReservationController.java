package com.example.api.reservation;

import com.example.api.member.MemberDTO;
import com.example.api.reservation.dto.CreateReservationReq;
import com.example.api.reservation.dto.GetReservationRes;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // todo memberController로 옮기기
    @GetMapping("/my/{status}")
    @Operation(summary = "나의 예약 조회", description = "방문 상태로 내가 예약한 가게를 조회할 수 있습니다.")
    public List<GetReservationRes> getMyReservations(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO,
                                                     @Parameter(description = "방문 상태는 소문자로 작성해주시면 됩니다.", required = true) @PathVariable ReservationStatus status) {

        GetMyReservationDTO dto = GetMyReservationDTO.builder()
                .memberId(memberDTO.getMemberId())
                .status(status)
                .build();

        return reservationService.getMyReservations(dto);
    }

    // todo 클래스 분리 및 이름 수정하기
    @Data
    public static class GetMyReservationDTO {
        private final long memberId;
        private final ReservationStatus status;

        @Builder
        public GetMyReservationDTO(long memberId, ReservationStatus status) {
            this.memberId = memberId;
            this.status = status;
        }
    }

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
}