package com.example.api.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원 프로필 수정 요청값")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberReq {

    @Schema(description = "닉네임", example = "수정할 닉네임입니다.")
    private String nickname;
}