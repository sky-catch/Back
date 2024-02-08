package com.example.core.oauth.infra.oauth.kakao.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;

@Data
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class KakaoToken {

    private String tokenType;
    private String accessToken;
    private String idToken;
    private String expiresIn;
    private String refreshToken;
    private String refreshTokenExpiresIn;
    private String scope;
}
