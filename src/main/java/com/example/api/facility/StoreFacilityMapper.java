package com.example.api.facility;

import com.example.api.facility.dto.Facility;
import com.example.api.facility.dto.FacilityReq;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreFacilityMapper {

    void createFacility(long restaurantId, List<Facility> facilities);

    void deleteFacility(FacilityReq dto);

    void updateFacility(long restaurantId, List<Long> facilityIds);
}