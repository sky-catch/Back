package com.example.api.mydining;

import com.example.api.member.MemberDTO;
import com.example.api.reservation.ReservationService;
import com.example.api.reservation.ReservationStatus;
import com.example.api.reservation.dto.MyReservationDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mydining")
@Tag(name = "마이다이닝", description = "마이다이닝 관련 API입니다.")
public class MyDiningController {

    private final ReservationService reservationService;

    @GetMapping("/my/{status}")
    @Operation(summary = "나의 예약 조회", description = "방문 상태로 내가 예약한 가게를 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 조회 성공", content = @Content(schema = @Schema(implementation = MyReservationDTO.class))),
    })
    public List<MyReservationDTO> getMyReservations(@LoginMember MemberDTO memberDTO,
                                                    @Parameter(description = "방문 상태") @PathVariable ReservationStatus status) {

        GetMyReservationDTO dto = GetMyReservationDTO.builder()
                .memberId(memberDTO.getMemberId())
                .status(status)
                .build();

        return reservationService.getMyReservations(dto);
    }
}
