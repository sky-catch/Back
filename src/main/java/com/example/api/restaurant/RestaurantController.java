package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantWithReview;
import com.example.api.restaurant.dto.UpdateRestaurantReq;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public void createRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner,
                                 @Valid @RequestBody CreateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        restaurantService.createRestaurant(dto);
    }

    @PutMapping("")
    @Operation(summary = "식당 수정", description = "전체 수정이므로 기존의 모든 데이터를 주셔야합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식당 수정 성공, 식당 편의시설 및 휴일도 수정 할 수 있습니다."),
            @ApiResponse(responseCode = "400", description = "서버에 중복된 식당 이름이 존재할 경우 발생"),
    })
    public void updateRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner,
                                 @Valid @RequestBody UpdateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        restaurantService.updateRestaurant(dto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "식당 조회", description = "식당을 조회하는 기능, 식당 사진 정렬 기준: 1. type(REPRESENTATION -> NORMAL 순서), 2. 등록일(오름차순)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식당 조회 성공, 식당 이미지, 공지사항, 편의시설, 리뷰가 함께 조회됩니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = " 식당 이름으로 식당을 찾지 못할 경우 발생"),
    })
    public ResponseEntity<GetRestaurantWithReview> getRestaurant(
            @Parameter(description = "조회하고 싶은 식당 이름", example = "스시미루") @PathVariable String name) {
        GetRestaurantWithReview restaurantRes = restaurantService.getRestaurantInfoByName(name);
        return ResponseEntity.ok(restaurantRes);
    }
}