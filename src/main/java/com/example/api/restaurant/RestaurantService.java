package com.example.api.restaurant;

import static com.example.api.restaurant.exception.RestaurantExceptionType.CAN_CREATE_ONLY_ONE;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_UNIQUE_NAME;

import com.example.api.facility.StoreFacilityMapper;
import com.example.api.facility.dto.Facility;
import com.example.api.facility.dto.FacilityReq;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.RestaurantDTO;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final StoreFacilityMapper storeFacilityMapper;

    @Transactional
    public long createRestaurant(RestaurantDTO dto, List<Facility> facility) {
        /*if (restaurantMapper.isAlreadyCreated(dto.getOwnerId())) {
            throw new SystemException(CAN_CREATE_ONLY_ONE.getMessage());
        }*/

        if (restaurantMapper.isAlreadyExistsName(dto.getName())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        restaurantMapper.save(dto);
        if(facility != null && !facility.isEmpty()){
            storeFacilityMapper.createFacility(dto.getRestaurantId(), facility);
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