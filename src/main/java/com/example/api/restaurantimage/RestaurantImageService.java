package com.example.api.restaurantimage;

import com.example.api.restaurant.RestaurantService;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.api.restaurantimage.dto.AddRestaurantImageWithTypeDTO;
import com.example.api.restaurantimage.dto.AddRestaurantImagesDTO;
import com.example.core.exception.SystemException;
import com.example.core.file.S3UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantImageService {

    private final RestaurantImageMapper restaurantImageMapper;
    private final RestaurantService restaurantService;
    private final S3UploadService s3UploadService;

    @Transactional
    public void addRestaurantImages(AddRestaurantImagesDTO dto, long ownerId) throws IOException {
        long restaurantId = dto.getRestaurantId();
        RestaurantDTO findRestaurant = restaurantService.getRestaurantById(restaurantId);
        if (!findRestaurant.isOwner(ownerId)) {
            throw new SystemException("식당 주인이 아닙니다.");
        }

        restaurantImageMapper.addRestaurantImages(restaurantId, getAddRestaurantImageWitTypeDTOS(dto));
    }

    private List<AddRestaurantImageWithTypeDTO> getAddRestaurantImageWitTypeDTOS(AddRestaurantImagesDTO dto)
            throws IOException {
        List<AddRestaurantImageWithTypeDTO> result = new ArrayList<>();
        for (int i = 0; i < dto.getFiles().size(); i++) {
            MultipartFile file = dto.getFiles().get(i);
            RestaurantImageType restaurantImageType = dto.getRestaurantImageTypes().get(i);
            result.add(AddRestaurantImageWithTypeDTO.builder()
                    .path(s3UploadService.upload(file))
                    .restaurantImageType(restaurantImageType)
                    .build());
        }
        return result;
    }
}
