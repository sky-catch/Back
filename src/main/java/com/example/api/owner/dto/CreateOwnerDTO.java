package com.example.api.owner.dto;

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
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateOwnerDTO extends BaseDTO {

    private long ownerId;
    private String name;
    private String profileImageUrl;
    private String email;
    private String phone;
    private OauthServerType platform;
    private HumanStatus status;
    private String businessRegistrationNumber;

    public String getPlatformName() {
        return platform.getValue();
    }
}