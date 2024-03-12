package com.example.api.owner.dto;

import com.example.core.dto.BaseDTO;
import com.example.core.dto.HumanStatus;
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
    private String platform;
    private HumanStatus status;
    private String businessRegistrationNumber;

    public Owner(CreateOwnerDTO createOwnerDTO) {
        this.name = createOwnerDTO.getName();
        this.imagePath = createOwnerDTO.getProfileImageUrl();
        this.email = createOwnerDTO.getEmail();
        this.platform = createOwnerDTO.getPlatformName();
        this.status = createOwnerDTO.getStatus();
        this.businessRegistrationNumber = createOwnerDTO.getBusinessRegistrationNumber();
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