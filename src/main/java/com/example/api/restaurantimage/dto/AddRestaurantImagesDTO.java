package com.example.api.restaurantimage.dto;

import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.core.exception.SystemException;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
            throw new SystemException("식당 이미지는 1 ~ 10개 사이여야 합니다.");
        }
    }

    private void validateImageWithType(List<MultipartFile> files, List<RestaurantImageType> restaurantImageTypes) {
        if (files.size() != restaurantImageTypes.size()) {
            throw new SystemException("식당 이미지와 식당 이미지 타입의 개수가 일치하지 않습니다.");
        }
    }

    private void validateImageType(List<RestaurantImageType> restaurantImageTypes) {
        long count = restaurantImageTypes.stream()
                .filter(restaurantImageType -> restaurantImageType == RestaurantImageType.REPRESENTATIVE)
                .count();
        if (count != 1) {
            throw new SystemException("대표 식당 이미지는 한 개여야 합니다.");
        }
    }
}
