package com.example.api.restaurantimage.dto;

import com.example.api.restaurant.dto.RestaurantImageType;
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
        this.restaurantId = restaurantId;
        this.files = files;
        this.restaurantImageTypes = restaurantImageTypes;
    }

    public boolean isEmptyImages() {
        return this.files.isEmpty() || restaurantImageTypes.isEmpty();
    }
}
