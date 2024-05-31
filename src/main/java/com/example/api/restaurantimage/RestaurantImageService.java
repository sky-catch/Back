package com.example.api.restaurantimage;

import com.example.api.restaurant.dto.RestaurantImageType;
import com.example.api.restaurantimage.dto.AddRestaurantImagesDTO;
import com.example.api.restaurantimage.dto.RestaurantImageDTO;
import com.example.core.file.S3UploadService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantImageService {

    private final RestaurantImageAddService restaurantImageAddService;
    private final S3UploadService s3UploadService;

    public void addRestaurantImages(AddRestaurantImagesDTO dto, long ownerId) {
        List<RestaurantImageDTO> restaurantImageDTOS = getRestaurantImageDTOS(dto);
        restaurantImageAddService.addRestaurantImages(dto, ownerId, restaurantImageDTOS);
    }

    private List<RestaurantImageDTO> getRestaurantImageDTOS(AddRestaurantImagesDTO dto) {
        List<RestaurantImageDTO> result = new ArrayList<>();

        Iterator<MultipartFile> fileIterator = dto.getFiles().iterator();
        Iterator<RestaurantImageType> typeIterator = dto.getRestaurantImageTypes().iterator();

        while (fileIterator.hasNext() && typeIterator.hasNext()) {
            MultipartFile file = fileIterator.next();
            RestaurantImageType type = typeIterator.next();
            result.add(RestaurantImageDTO.builder()
                    .path(s3UploadService.upload(file))
                    .restaurantImageType(type)
                    .build());
        }

        return result;
    }
}
