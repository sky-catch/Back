package com.example.api.facility;

import com.example.api.facility.dto.Facility;
import com.example.api.facility.dto.FacilityReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreFacilityMapper {

    void createFacility(long restaurantId, List<Facility> facilities);

    void deleteFacility(FacilityReq dto);
}