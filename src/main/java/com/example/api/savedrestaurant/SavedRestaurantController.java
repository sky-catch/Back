package com.example.api.savedrestaurant;

import com.example.api.member.MemberDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/savedRestaurant")
@Tag(name = "식당 저장", description = "'식당 저장' 관련 API입니다.")
public class SavedRestaurantController {

    private final SavedRestaurantService savedRestaurantService;

    @PostMapping("/{restaurantId}")
    @Operation(summary = "'식당 저장' 생성", description = "restaurantId로 '식당 저장'을 생성하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "'식당 저장' 성공, 식당의 저장 개수 +1"),
            @ApiResponse(responseCode = "404", description = "restaurantId로 식당을 찾지 못한 경우"),
            @ApiResponse(responseCode = "409", description = "사용자가 해당 식당을 이미 저장한 경우"),
    })
    public ResponseEntity<Void> createReservation(@LoginMember MemberDTO loginMember,
                                                  @Parameter(description = "저장하고 싶은 식당 ID", example = "1") @PathVariable long restaurantId) {

        CreateSavedRestaurantDTO dto = CreateSavedRestaurantDTO.builder()
                .memberId(loginMember.getMemberId())
                .restaurantId(restaurantId)
                .build();

        savedRestaurantService.createSavedRestaurant(dto);

        URI uri = URI.create("/savedRestaurant/" + restaurantId);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "'식당 저장' 삭제", description = "restaurantId로 '식당 저장'을 삭제하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "'식당 저장' 삭제 성공, 식당의 저장 개수 -1"),
            @ApiResponse(responseCode = "404", description = "restaurantId로 식당을 찾지 못한 경우"),
    })
    public void deleteSavedRestaurant(@LoginMember MemberDTO loginMember,
                                      @Parameter(description = "삭제하고 싶은 식당 ID", example = "1") @PathVariable long restaurantId) {

        DeleteSavedRestaurantDTO dto = DeleteSavedRestaurantDTO.builder()
                .memberId(loginMember.getMemberId())
                .restaurantId(restaurantId)
                .build();

        savedRestaurantService.deleteSavedRestaurant(dto);
    }
}