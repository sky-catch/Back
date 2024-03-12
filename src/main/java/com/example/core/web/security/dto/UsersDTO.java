package com.example.core.web.security.dto;

import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Member, Owner를 추상화한 개념
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UsersDTO extends BaseDTO {

    private String email;
    private boolean isOwner;

    public static UsersDTO of(MemberDTO memberDTO) {
        return new UsersDTO(memberDTO.getEmail(), false);
    }

    public static UsersDTO of(GetOwnerRes getOwnerRes) {
        return new UsersDTO(getOwnerRes.getEmail(), true);
    }
}
