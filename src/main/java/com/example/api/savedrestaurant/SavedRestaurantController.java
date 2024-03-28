package com.example.api.savedrestaurant;

import com.example.api.member.MemberDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/savedRestaurant")
@Tag(name = "식당 저장", description = "식당 저장 관련 API입니다.")
public class SavedRestaurantController {

    private final SavedRestaurantService savedRestaurantService;

    @PostMapping("/{restaurantId}")
    @Operation(summary = "식당 저장 생성", description = "식당 저장을 생성하는 API입니다.")
    public ResponseEntity<Void> createReservation(@Parameter(hidden = true) @LoginMember MemberDTO loginMember,
                                                  @PathVariable long restaurantId) {

        CreateSavedRestaurantDTO dto = CreateSavedRestaurantDTO.builder()
                .memberId(loginMember.getMemberId())
                .restaurantId(restaurantId)
                .build();

        savedRestaurantService.createSavedRestaurant(dto);

        URI uri = URI.create("/savedRestaurant/" + restaurantId);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{restaurantId}")
    @Operation(summary = "식당 저장 삭제", description = "식당 저장을 삭제하는 API입니다.")
    public void deleteSavedRestaurant(@Parameter(hidden = true) @LoginMember MemberDTO loginMember,
                                      @PathVariable long restaurantId) {

        DeleteSavedRestaurantDTO dto = DeleteSavedRestaurantDTO.builder()
                .memberId(loginMember.getMemberId())
                .restaurantId(restaurantId)
                .build();

        savedRestaurantService.deleteSavedRestaurant(dto);
    }
}