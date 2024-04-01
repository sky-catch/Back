package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantWithReview;
import com.example.api.restaurant.dto.UpdateRestaurantReq;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Tag(name = "식당", description = "식당 관련 API입니다.")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // todo 이미지 생성도 여기서 하기
    @PostMapping
    @Operation(summary = "식당 생성", description = "사장은 식당을 생성할 수 있습니다.")
    public void createRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner,
                                 @Valid @RequestBody CreateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        restaurantService.createRestaurant(dto);
    }

    @PutMapping("")
    @Operation(summary = "식당 수정", description = "전체 수정이므로 기존의 모든 데이터를 주셔야합니다.")
    public void updateRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner,
                                 @Valid @RequestBody UpdateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        restaurantService.updateRestaurant(dto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "식당 조회", description = "식당을 조회하는 기능, 식당 사진 정렬 기준: 1. type(REPRESENTATION -> NORMAL 순서), 2. 등록일(오름차순)")
    public ResponseEntity<GetRestaurantWithReview> getRestaurant(@PathVariable String name) {
        GetRestaurantWithReview restaurantRes = restaurantService.getRestaurantInfoByName(name);
        return ResponseEntity.ok(restaurantRes);
    }
}