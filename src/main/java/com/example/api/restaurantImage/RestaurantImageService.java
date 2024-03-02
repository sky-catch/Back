package com.example.api.restaurantImage;

import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.api.restaurantImage.dto.AddRestaurantImageWithTypeDTO;
import com.example.api.restaurantImage.dto.AddRestaurantImagesDTO;
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
    private final S3UploadService s3UploadService;

    @Transactional
    public void addRestaurantImages(AddRestaurantImagesDTO dto) throws IOException {
        if (dto.isEmptyImages()) {
            throw new SystemException("식당 이미지는 한 개 이상 존재해야 합니다.");
        }

        restaurantImageMapper.addRestaurantImages(dto.getRestaurantId(), getAddRestaurantImageWitTypeDTOS(dto));
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
