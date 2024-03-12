package com.example.core.oauth.dto;

import com.example.core.web.security.jwt.dto.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private AccessToken accessToken;
    private boolean isOwner;
}
