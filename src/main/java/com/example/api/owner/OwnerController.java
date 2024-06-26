package com.example.api.owner;

import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.CreateOwnerReq;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.GetReservationOfRestaurantRes;
import com.example.api.owner.dto.Owner;
import com.example.api.reservation.ReservationService;
import com.example.api.reservation.dto.request.ChangeReservationsStatusToNoShowReq;
import com.example.api.restaurant.dto.GetRestaurantInfo;
import com.example.core.exception.ExceptionResponse;
import com.example.core.web.security.login.LoginMember;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
@Tag(name = "사장")
public class OwnerController {

    private final OwnerService ownerService;
    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "사장 생성", description = "사장 생성은 로그인 후 할 수 있습니다. 사업장등록번호의 마지막 숫자는 5여야합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사장 생성 성공"),
            @ApiResponse(responseCode = "400", description = "기존에 사장을 생성한 경우 발생"),
    })
    public void createOwner(@LoginMember MemberDTO memberDTO, @Valid @RequestBody CreateOwnerReq req) {
        ownerService.createOwner(memberDTO, req.getBusinessRegistrationNumber());
    }

    @GetMapping("")
    @Operation(summary = "사장 조회")
    public GetOwnerRes getOwner(@LoginOwner Owner owner) {
        return ownerService.getOwner(owner.getOwnerId());
    }

    /**
     * delete로 변경
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "사장 삭제")
    public void deleteOwner(@LoginOwner Owner owner,
                            @Parameter(description = "사장 ID", example = "1") @PathVariable(name = "id") long ownerId) {
        ownerService.deleteOwner(ownerId);
    }

    @GetMapping("/restaurant")
    @Operation(summary = "내 식당 보기")
    public GetRestaurantInfo getMyRestaurantInfo(@LoginOwner Owner owner) {
        return ownerService.getRestaurantInfoByOwnerId(owner.getOwnerId());
    }

    @PatchMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "예약들 노쇼로 바꾸는 기능", description = "예약을 노쇼한 경우 사장이 직접 예약 상태를 방문 예정에서 노쇼로 바꾸는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "요청한 예약 번호들 중 예약 상태가 방문 예정이 아닌 경우 발생하는 에러입니다.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "사장이 DB에 없는 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public void changeReservationStatusToNoShow(@LoginOwner Owner owner,
                                                @Valid @RequestBody ChangeReservationsStatusToNoShowReq req) {

        reservationService.changeReservationsToNoShowByRequest(req);
    }

    @GetMapping("/reservation")
    @Operation(summary = "식당의 예약 보기", description = "예약 리스트들과 예약 상태별 숫자를 볼 수 있습니다.")
    public GetReservationOfRestaurantRes getReservationOfRestaurant(@LoginOwner Owner owner){
        return reservationService.getReservationOfRestaurant(owner.getOwnerId());
    }

}