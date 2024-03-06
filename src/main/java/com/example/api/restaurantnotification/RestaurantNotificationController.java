package com.example.api.restaurantnotification;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.RestaurantNotificationDTO;
import com.example.api.restaurantnotification.dto.CreateRestaurantNotificationDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants/{restaurantId}/notifications")
@RequiredArgsConstructor
@Tag(name = "식당 공지사항", description = "식당 공지사항 관련 API입니다.")
public class RestaurantNotificationController {

    private final RestaurantNotificationService restaurantNotificationService;

    @PostMapping
    @Operation(summary = "식당 공지사항 생성", description = "사장은 식당 공지사항을 생성할 수 있습니다.")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "식당 공지사항 생성 성공")
    )
    public ResponseEntity<Void> createRestaurantNotification(@Parameter(hidden = true) @LoginMember Owner owner,
                                                             @PathVariable long restaurantId,
                                                             @RequestBody CreateRestaurantNotificationDTO dto) {

        RestaurantNotificationDTO restaurantNotificationDTO = RestaurantNotificationDTO.builder()
                .restaurantId(restaurantId)
                .ownerId(owner.getOwnerId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        long createdRestaurantNotificationId = restaurantNotificationService.createRestaurantNotification(
                restaurantNotificationDTO);

        URI uri = URI.create("/restaurants/" + restaurantId + "/notifications/" + createdRestaurantNotificationId);
        return ResponseEntity.created(uri).build();
    }
}