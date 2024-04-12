package com.example.api.facility.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "식당 편의시설 응답값")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFacilityRes {
    @Schema(description = "부대시설 이름", example = "주차 가능")
    private String name;
    @Schema(description = "부대시설 사진 경로", example = "https://skyware-toy-project-imgae-bucket.s3.ap-northeast-2.amazonaws.com/facility-icon/ic_parking.svg")
    private String path;
}
