package com.example.api.owner.dto;

import com.example.api.member.MemberDTO;
import com.example.core.dto.BaseDTO;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Owner extends BaseDTO {

    private long ownerId;
    private String name;
    private String imagePath;
    private String email;
    private OauthServerType oauthServer;
    private HumanStatus status;
    private String businessRegistrationNumber;

    public Owner(MemberDTO memberDTO, String businessRegistrationNumber) {
        this.name = memberDTO.getName();
        this.imagePath = memberDTO.getProfileImageUrl();
        this.email = memberDTO.getEmail();
        this.oauthServer = memberDTO.getOauthServer();
        this.status = memberDTO.getStatus();
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public Owner(UpdateOwnerReq ownerReq) {
        this.ownerId = ownerReq.getOwnerId();
        this.name = ownerReq.getName();
        this.email = ownerReq.getEmail();
    }

    public GetOwnerRes toDto() {
        return GetOwnerRes.builder().ownerId(ownerId).name(name).imagePath(imagePath).email(email).status(status)
                .createdDate(getCreatedDate()).updatedDate(getUpdatedDate()).build();
    }

}