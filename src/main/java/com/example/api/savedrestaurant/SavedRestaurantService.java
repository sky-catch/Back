package com.example.api.savedrestaurant;

import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedRestaurantService {

    private final SavedRestaurantMapper savedRestaurantMapper;

    @Transactional
    public void createSavedRestaurant(CreateSavedRestaurantDTO dto) {
        if (savedRestaurantMapper.isAlreadyExistsByRestaurantIdAndMemberId(dto.getRestaurantId(), dto.getMemberId())) {
            throw new SystemException("해당 식당은 이미 저장하였습니다.");
        }

        SavedRestaurantDTO savedRestaurant = dto.toSavedRestaurantDTO();

        savedRestaurantMapper.save(savedRestaurant);
    }
}