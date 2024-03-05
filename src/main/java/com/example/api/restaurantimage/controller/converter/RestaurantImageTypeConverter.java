package com.example.api.restaurantimage.controller.converter;

import com.example.api.restaurant.dto.RestaurantImageType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RestaurantImageTypeConverter implements Converter<String, List<RestaurantImageType>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public List<RestaurantImageType> convert(String s) {
        return objectMapper.readValue(s, new TypeReference<List<RestaurantImageType>>() {

        });
    }
}
