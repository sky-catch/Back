package com.example.api.restaurantImage.controller;

import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.api.restaurantImage.RestaurantImageService;
import com.example.api.restaurantImage.dto.AddRestaurantImagesDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/restaurants/{restaurantId}/images")
@RequiredArgsConstructor
@Tag(name = "식당 이미지", description = "식당 이미지 관련 API입니다.")
public class RestaurantImageController {

    private final RestaurantService restaurantService;
    private final RestaurantImageService restaurantImageService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "식당 이미지들 추가", description = "식당의 이미지들을 추가하는 기능입니다.")
    public ResponseEntity<Void> addRestaurantImages(@Parameter(hidden = true) @LoginMember Owner owner,
                                                    @PathVariable long restaurantId,
                                                    @Parameter @RequestParam List<RestaurantImageType> restaurantImageTypes,
                                                    @Parameter(description = "이미지 형식의 파일만 가능, 최소 1개 이상") @RequestPart List<MultipartFile> files)
            throws IOException {

        // todo 식당 이미지 개수 제한 어떻게 할까
//        if (files.size() > 5) {
//            throw new SystemException("이미지 개수는 5개를 넘을 수 없습니다.");
//        }

        // 1. 실존하는 식당인지 확인
        RestaurantDTO restaurant = restaurantService.getRestaurantById(restaurantId);

        // 2. 식당 주인인지 확인
        restaurantService.isOwner(restaurant, owner);

        AddRestaurantImagesDTO dto = AddRestaurantImagesDTO.builder()
                .restaurantId(restaurant.getRestaurantId())
                .files(files)
                .restaurantImageTypes(restaurantImageTypes)
                .build();
        restaurantImageService.addRestaurantImages(dto);

        URI uri = URI.create("/restaurants/" + restaurantId + "/images");
        return ResponseEntity.created(uri).build();
    }
}