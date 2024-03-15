package com.example.api.facility.dto;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO extends BaseDTO {

    @JsonIgnore
    private long facilityId;
    private String name;
    private String path;
}