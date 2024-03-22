package com.example.core.web.security.dto;

import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.core.dto.HumanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Member, Owner를 추상화한 개념
 */
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDTO {

    private long id;
    private String nickname;
    private String profileImageUrl;
    private String email;
    private String name;
    private HumanStatus status;
    private boolean isOwner;

    public static UsersDTO createNotOwner(MemberDTO memberDTO) {
        return UsersDTO.builder()
                .id(memberDTO.getMemberId())
                .nickname(memberDTO.getNickname())
                .profileImageUrl(memberDTO.getProfileImageUrl())
                .email(memberDTO.getEmail())
                .name(memberDTO.getName())
                .status(memberDTO.getStatus())
                .isOwner(false)
                .build();
    }

    public static UsersDTO createOwner(GetOwnerRes getOwnerRes) {
        return UsersDTO.builder()
                .id(getOwnerRes.getOwnerId())
                .nickname(getOwnerRes.getName())
                .profileImageUrl(getOwnerRes.getImagePath())
                .email(getOwnerRes.getEmail())
                .name(getOwnerRes.getName())
                .status(getOwnerRes.getStatus())
                .isOwner(true)
                .build();
    }

    public void setMemberToOwner() {
        this.isOwner = true;
    }
}
