package com.example.core.oauth.dto;

import com.example.core.dto.BaseDTO;
import com.example.core.web.security.jwt.dto.AccessToken;
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
public class LoginResponse extends BaseDTO {

    private AccessToken accessToken;
    private boolean isOwner;
}
