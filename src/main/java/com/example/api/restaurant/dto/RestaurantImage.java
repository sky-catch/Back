package com.example.api.restaurant.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class RestaurantImage extends BaseDTO {

    private long restaurantImageId;
    @JsonIgnore
    private long restaurantId;
    private String path;
    private RestaurantImageType type;
}