package com.example.api.facility;

import com.example.api.facility.dto.FacilityReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facility")
@Tag(name = "시설")
public class StoreFacilityController {

    private final StoreFacilityService storeFacilityService;

    @Operation(summary = "시설 제거", description = "식당 생성에서 형식 확인")
    @DeleteMapping("")
    public void deleteFacility(@RequestBody FacilityReq dto){
        storeFacilityService.deleteFacility(dto);
    }
}
