package com.example.api.owner.dto;

import com.example.api.owner.dto.validation.BusinessRegistrationNumberCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사장 생성 요청값")
public class CreateOwnerReq {

    @BusinessRegistrationNumberCheck
    @Schema(description = "사업장등록번호", example = "101-01-00015")
    private String businessRegistrationNumber;
}
