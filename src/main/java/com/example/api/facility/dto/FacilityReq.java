package com.example.api.facility.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityReq {

    private long restaurantId;
    @Schema(example = "[\"PARKING\", \"CORKAGE\"]", description = "시설들")
    private List<Facility> facilities;
}