package com.example.api.restaurantimage.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.api.restaurantimage.RestaurantImageService;
import com.example.api.restaurantimage.dto.AddRestaurantImagesDTO;
import com.example.core.web.security.login.LoginOwner;
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

    @Operation(summary = "식당 이미지들 추가", description = "사장이 식당의 이미지들을 추가하는 기능입니다. ImageTypes의 개수와 이미지 파일의 개수는 일치해야 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "식당 이미지들 생성 성공"),
            @ApiResponse(responseCode = "400", description = "1. 식당 이미지 개수가 1 ~ 10개 사이가 아닌 경우,\n"
                    + "2. addRestaurantImagesReq의 restaurantImageTypes와 files의 수가 일치하지 않는 경우,\n"
                    + "3. addRestaurantImagesReq의 restaurantImageTypes에 `REPRESENTATIVE가` 존재하지 않는 경우,\n"
                    + "4. 사장의 요청이 아닌 경우 발생합니다."),
    })
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> addRestaurantImages(@LoginOwner Owner owner,
                                                    @Parameter(description = "식당 이미지들을 추가할 식당 ID", example = "1") @PathVariable long restaurantId,
                                                    @RequestPart AddRestaurantImagesReq addRestaurantImagesReq,
                                                    @Parameter(description = "식당 이미지 파일들", example = "확장자는 .jpg, .png처럼 이미지 확장자를 허용합니다.") @RequestPart List<MultipartFile> files)
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

    @Schema(description = "식당 이미지들 생성 요청값, 대표 이미지 - REPRESENTATIVE, 일반 이미지 - NORMAL")
    @EqualsAndHashCode
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRestaurantImagesReq {

        @NotNull
        @Schema(description = "식당 이미지 타입", example = "[REPRESENTATIVE, NORMAL]")
        private List<RestaurantImageType> restaurantImageTypes;
    }
}