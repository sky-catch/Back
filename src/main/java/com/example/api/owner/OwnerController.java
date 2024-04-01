package com.example.api.owner;

import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.CreateOwnerReq;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.GetRestaurantWithReview;
import com.example.core.web.security.login.LoginMember;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping
    @Operation(summary = "사장 생성", description = "사장 생성은 로그인 후 할 수 있습니다. 사업장등록번호의 마지막 숫자는 5여야합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOwner(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO,
                            @Valid @RequestBody CreateOwnerReq req) {
        ownerService.createOwner(memberDTO, req.getBusinessRegistrationNumber());
    }

    @GetMapping("")
    @Operation(summary = "사장 조회")
    public GetOwnerRes getOwner(@Parameter(hidden = true) @LoginOwner Owner owner) {
        return ownerService.getOwner(owner.getOwnerId());
    }

    /**
     * delete문이 아닌 status 변화
     */
    @PatchMapping("/{id}")
    @Operation(summary = "사장 삭제")
    public void deleteOwner(@Parameter(hidden = true) @LoginOwner Owner owner, @PathVariable(name = "id") long ownerId) {
        ownerService.deleteOwner(ownerId);
    }

    @GetMapping("/restaurant")
    @Operation(summary = "내 식당 보기")
    public GetRestaurantWithReview getMyRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner) {
        return ownerService.getRestaurantByOwnerId(owner.getOwnerId());
    }

}