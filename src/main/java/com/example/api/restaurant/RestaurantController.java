package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Tag(name = "식당", description = "식당 관련 API입니다.")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @Operation(summary = "식당 생성", description = "사장은 식당을 생성할 수 있습니다.")
    public ResponseEntity<Void> createRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner,
                                                 @Valid @RequestBody CreateRestaurantReq dto) {
        dto.setOwnerId(owner.getOwnerId());
        long createdRestaurantId = restaurantService.createRestaurant(dto);
        URI uri = URI.create("/restaurants/" + createdRestaurantId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{name}")
    @Operation(summary = "식당 조회", description = "식당을 조회하는 기능, 식당 사진 정렬 기준: 1. type(REPRESENTATION -> NORMAL 순서), 2. 등록일(오름차순)")
    public ResponseEntity<GetRestaurantRes> getRestaurant(@PathVariable String name) {
        GetRestaurantRes restaurantRes = restaurantService.getRestaurantInfoByName(name);

        return ResponseEntity.ok(restaurantRes);
    }
}