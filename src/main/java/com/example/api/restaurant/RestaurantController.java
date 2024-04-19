package com.example.api.restaurant;

import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.search.SearchFilter;
import com.example.api.restaurant.dto.search.GetRestaurantSearchRes;
import com.example.api.restaurant.dto.search.GetRestaurantSearchSummaryRes;
import com.example.core.exception.SystemException;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantInfo;
import com.example.api.restaurant.dto.UpdateRestaurantReq;
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
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Tag(name = "식당", description = "식당 관련 API입니다.")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @Operation(summary = "식당 생성", description = "사장은 식당을 생성할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식당 생성 성공, 식당 편의시설도 생성됩니다."),
            @ApiResponse(responseCode = "400", description = "기존에 식당을 생성한 경우, 서버에 중복된 식당 이름이 존재할 경우 발생"),
    })
    public void createRestaurant(@LoginOwner Owner owner, @Valid @RequestBody CreateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        restaurantService.createRestaurant(dto);
    }

    @PutMapping("")
    @Operation(summary = "식당 수정", description = "전체 수정이므로 기존의 모든 데이터를 주셔야합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식당 수정 성공, 식당 편의시설 및 휴일도 수정 할 수 있습니다."),
            @ApiResponse(responseCode = "400", description = "서버에 중복된 식당 이름이 존재할 경우 발생"),
    })
    public void updateRestaurant(@LoginOwner Owner owner, @Valid @RequestBody UpdateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        restaurantService.updateRestaurant(dto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "이름으로 식당 상세 조회", description = "이름으로 식당 상세 정보를 조회하는 기능, 식당 사진 정렬 기준: 1. type(REPRESENTATION -> NORMAL 순서), 2. 등록일(오름차순)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식당 상세 조회 성공, 식당 이미지/공지사항/편의시설/리뷰/로그인한 경우 로그인한 회원의 식당 저장 여부가 함께 조회됩니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = " 식당 이름으로 식당을 찾지 못할 경우 발생", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<GetRestaurantInfo> getRestaurantInfoByName(
            @LoginMember(required = false) MemberDTO loginMember,
            @Parameter(description = "조회하고 싶은 식당 이름", example = "스시미루") @PathVariable String name) {
        GetRestaurantInfo restaurantInfo = restaurantService.getRestaurantInfoByName(name, loginMember.getMemberId());
        return ResponseEntity.ok(restaurantInfo);
    }

    @GetMapping("/search/{keyword}")
    public GetRestaurantSearchSummaryRes getRestaurantSearchSummary(@Parameter(description = "keyword는 두 글자 이상으로 해주세요.") @PathVariable String keyword){
        if(keyword.length() < 2){
            throw new SystemException("keyword는 두 글자 이상으로 해주세요.");
        }
        return restaurantService.getSearchSummaryList(keyword);
    }

    @GetMapping("/search")
    public GetRestaurantSearchRes getRestaurantSearchList(@RequestBody SearchFilter searchFilter,
                                                          @LoginMember(required = false) MemberDTO memberDTO){
        //error
        //return restaurantService.getSearchList(searchFilter, memberDTO.getMemberId());
        return null;
    }
}