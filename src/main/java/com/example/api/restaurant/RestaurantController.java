package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Void> createRestaurant(@Parameter(hidden = true) @LoginOwner Owner owner,
                                                 @Valid @RequestBody CreateRestaurantReq dto) {
        RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                .ownerId(owner.getOwnerId())
                .name(dto.getName())
                .category(dto.getCategory())
                .content(dto.getContent())
                .phone(dto.getPhone())
                .capacity(dto.getCapacity())
                .openTime(dto.getOpenTime())
                .lastOrderTime(dto.getLastOrderTime())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .lunchPrice(dto.getLunchPrice())
                .dinnerPrice(dto.getDinnerPrice())
                .reviewCount(dto.getReviewCount())
                .build();
        long createdRestaurantId = restaurantService.createRestaurant(restaurantDTO);
        URI uri = URI.create("/restaurants/" + createdRestaurantId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "식당 조회", description = "식당을 조회하는 기능, 정렬 기준: 1. type(REPRESENTATION, NORMAL 순서), 2. 등록일(오름차순)")
    public ResponseEntity<GetRestaurantRes> getRestaurant(@PathVariable long restaurantId) {
        GetRestaurantRes restaurantRes = restaurantService.getRestaurantInfoById(restaurantId);

        return ResponseEntity.ok(restaurantRes);
    }
}