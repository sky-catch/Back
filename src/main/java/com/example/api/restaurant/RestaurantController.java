package com.example.api.restaurant;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.web.security.login.LoginMember;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@LoginMember Owner owner, @RequestBody CreateRestaurantReq dto) {

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
}