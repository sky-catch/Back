package com.example.api.restaurant.dto;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

// todo 메뉴로 이름 수정 필요
public class RestaurantImageDTO extends BaseDTO {

    private long menuId;
    private long restaurantId;
    private String path;
    private RestaurantImageType type;
}