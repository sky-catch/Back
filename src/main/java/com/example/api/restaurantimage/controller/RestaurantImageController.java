package com.example.api.restaurantimage.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.api.restaurantimage.RestaurantImageService;
import com.example.api.restaurantimage.dto.AddRestaurantImagesDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/restaurants/{restaurantId}/images")
@RequiredArgsConstructor
@Tag(name = "식당 이미지", description = "식당 이미지 관련 API입니다.")
public class RestaurantImageController {

    private final RestaurantImageService restaurantImageService;

    @Operation(summary = "식당 이미지들 추가", description = "식당의 이미지들을 추가하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "식당 이미지들 생성 성공"),
    })
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> addRestaurantImages(@Parameter(hidden = true) @LoginMember Owner owner,
                                                    @PathVariable long restaurantId,
                                                    @Parameter @RequestPart AddRestaurantImagesReq addRestaurantImagesReq,
                                                    @RequestPart List<MultipartFile> files)
            throws IOException {

        AddRestaurantImagesDTO dto = AddRestaurantImagesDTO.builder()
                .restaurantId(restaurantId)
                .files(files)
                .restaurantImageTypes(addRestaurantImagesReq.restaurantImageTypes)
                .build();
        restaurantImageService.addRestaurantImages(dto, owner.getOwnerId());

        URI uri = URI.create("/restaurants/" + restaurantId + "/images");
        return ResponseEntity.created(uri).build();
    }

    @Schema(description = "식당 이미지들 생성 요청값")
    @EqualsAndHashCode
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRestaurantImagesReq {

        @NotNull
        @Schema(description = "식당 이미지 타입", example = "대표 이미지 - REPRESENTATIVE, 일반 이미지 - NORMAL")
        private List<RestaurantImageType> restaurantImageTypes;
    }
}