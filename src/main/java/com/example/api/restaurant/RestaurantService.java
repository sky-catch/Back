package com.example.api.restaurant;

import com.example.api.facility.StoreFacilityMapper;
import com.example.api.facility.dto.Facility;
import com.example.api.restaurant.dto.CreateRestaurantReq;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.api.restaurant.exception.RestaurantExceptionType.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final StoreFacilityMapper storeFacilityMapper;

    @Transactional
    public long createRestaurant(CreateRestaurantReq req) {

        RestaurantDTO dto = new RestaurantDTO(req);
        if (restaurantMapper.isAlreadyCreated(dto.getOwnerId())) {
            throw new SystemException(CAN_CREATE_ONLY_ONE.getMessage());
        }

        if (restaurantMapper.isAlreadyExistsName(dto.getName())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        restaurantMapper.save(dto);
        if(req.getFacilities() != null && !req.getFacilities().isEmpty()){
            storeFacilityMapper.createFacility(dto.getRestaurantId(), req.getFacilities());
        }

        return dto.getRestaurantId();
    }

    @Transactional(readOnly = true)
    public RestaurantDTO getRestaurantById(long restaurantId) {
        return restaurantMapper.findById(restaurantId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    public GetRestaurantRes getRestaurantInfoById(long restaurantId) {
        GetRestaurantRes getRestaurantRes = restaurantMapper.findRestaurantInfoById(restaurantId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
//        getRestaurantRes.sortImages();

        return getRestaurantRes;
    }

    @Transactional(readOnly = true)
    public GetRestaurantRes getRestaurantInfoByName(String name) {
        return restaurantMapper.findRestaurantInfoByName(name)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
    }
}