package com.example.api.member;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO extends BaseDTO {

    private long memberId;
    private String name;
    private String imagePath;
    private String nickname;
    private String phone;
    private String email;
    private String platform;
    private String status;
}