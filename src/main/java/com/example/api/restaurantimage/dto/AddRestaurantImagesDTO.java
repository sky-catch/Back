package com.example.api.restaurantimage.dto;

import static com.example.api.restaurantimage.exception.RestaurantImageExceptionType.NOT_MATCH_IMAGE_SIZE_WITH_IMAGE_TYPE_SIZE;
import static com.example.api.restaurantimage.exception.RestaurantImageExceptionType.NOT_SATISFIED_IMAGE_SIZE;
import static com.example.api.restaurantimage.exception.RestaurantImageExceptionType.NOT_SATISFIED_IMAGE_TYPE_REQUIREMENT;

import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.core.exception.SystemException;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Data
@NoArgsConstructor
public class AddRestaurantImagesDTO {
    private long restaurantId;
    private List<MultipartFile> files;
    private List<RestaurantImageType> restaurantImageTypes;

    @Builder
    public AddRestaurantImagesDTO(long restaurantId, List<MultipartFile> files,
                                  List<RestaurantImageType> restaurantImageTypes) {
        validateListSize(files);
        validateListSize(restaurantImageTypes);
        validateImageWithType(files, restaurantImageTypes);
        validateImageType(restaurantImageTypes);

        this.restaurantId = restaurantId;
        this.files = files;
        this.restaurantImageTypes = restaurantImageTypes;
    }

    private void validateListSize(List<?> list) {
        if (list.isEmpty() || list.size() > 10) {
            throw new SystemException(NOT_SATISFIED_IMAGE_SIZE);
        }
    }

    private void validateImageWithType(List<MultipartFile> files, List<RestaurantImageType> restaurantImageTypes) {
        if (files.size() != restaurantImageTypes.size()) {
            log.error("식당 이미지 파일 개수 = {}, 식당 이미지 파일 타입 개수 = {}", files.size(), restaurantImageTypes.size());
            files.stream().map(MultipartFile::getOriginalFilename)
                    .forEach(fileName -> log.error("식당 이미지 파일 이름 : {}", fileName));
            restaurantImageTypes.stream().map(Enum::name)
                    .forEach(typeName -> log.error("식당 이미지 파일 타입 이름 : {}", typeName));
            throw new SystemException(NOT_MATCH_IMAGE_SIZE_WITH_IMAGE_TYPE_SIZE);
        }
    }

    private void validateImageType(List<RestaurantImageType> restaurantImageTypes) {
        long count = restaurantImageTypes.stream()
                .filter(restaurantImageType -> restaurantImageType == RestaurantImageType.REPRESENTATIVE)
                .count();
        if (count != 1) {
            throw new SystemException(NOT_SATISFIED_IMAGE_TYPE_REQUIREMENT);
        }
    }
}
