package com.example.api.facility;

import com.example.api.facility.dto.FacilityReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreFacilityService {

    private final StoreFacilityMapper storeFacilityMapper;

    public void deleteFacility(FacilityReq dto) {
        storeFacilityMapper.deleteFacility(dto);
    }
}