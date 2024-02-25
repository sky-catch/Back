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
    private String phone;
    private String email;
    private String platform;
    private HumanStatus status;

    public Owner(CreateOwnerReq ownerReq){
        this.name = ownerReq.getName();
        this.phone = ownerReq.getPhone();
        this.email = ownerReq.getEmail();
    }

    public Owner(UpdateOwnerReq ownerReq){
        this.ownerId = ownerReq.getOwnerId();
        this.name = ownerReq.getName();
        this.phone = ownerReq.getPhone();
        this.email = ownerReq.getEmail();
    }

    public GetOwnerRes toDto(){
        return GetOwnerRes.builder().ownerId(ownerId).name(name).imagePath(imagePath).phone(phone)
                .email(email).status(status).createdDate(getCreatedDate()).updatedDate(getUpdatedDate()).build();
    }

}