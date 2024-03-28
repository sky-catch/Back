package com.example.api.savedrestaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteSavedRestaurantDTO {
    private long memberId;
    private long restaurantId;

    public SavedRestaurantDTO toSavedRestaurantDTO() {
        return SavedRestaurantDTO.builder()
                .memberId(memberId)
                .restaurantId(restaurantId)
                .build();
    }
}
