package com.example.core.oauth.infra.oauth.kakao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoOauthConfig {

    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String[] scope;
}
