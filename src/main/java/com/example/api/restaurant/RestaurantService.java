package com.example.api.restaurant;

import static com.example.api.restaurant.exception.RestaurantExceptionType.CAN_CREATE_ONLY_ONE;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_UNIQUE_NAME;

import com.example.api.facility.StoreFacilityMapper;
import com.example.api.holiday.HolidayDTO;
import com.example.api.holiday.HolidayMapper;
import com.example.api.reservationavailabledate.ReservationAvailableDateDTO;
import com.example.api.reservationavailabledate.ReservationAvailableDateMapper;
import com.example.api.restaurant.dto.*;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final StoreFacilityMapper storeFacilityMapper;
    private final HolidayMapper holidayMapper;
    private final ReservationAvailableDateMapper reservationAvailableDateMapper;

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
        if (req.getFacilities() != null && !req.getFacilities().isEmpty()) {
            storeFacilityMapper.createFacility(dto.getRestaurantId(), req.getFacilities());
        }

        return dto.getRestaurantId();
    }

    @Transactional
    public void updateRestaurant(UpdateRestaurantReq req) {
        RestaurantDTO dto = new RestaurantDTO(req);

        if (restaurantMapper.isAlreadyExistsNameExcludeSelf(dto.getName(), dto.getRestaurantId())) {
            throw new SystemException(NOT_UNIQUE_NAME.getMessage());
        }

        restaurantMapper.updateRestaurant(dto);

        List<HolidayDTO> holidayDTOs = req.getDays().getDays().stream()
                .map(day -> new HolidayDTO(dto.getRestaurantId(), day))
                .collect(Collectors.toList());

        holidayMapper.delete(dto.getRestaurantId());
        holidayMapper.saveAll(holidayDTOs);
        reservationAvailableDateMapper.update(new ReservationAvailableDateDTO(req));

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

    @Transactional(readOnly = true)
    public RestaurantWithHolidayAndAvailableDateDTO getRestaurantWithHolidayAndAvailableDateById(long restaurantId) {
        return restaurantMapper.findRestaurantWithHolidayAndAvailableDateById(restaurantId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
    }

}