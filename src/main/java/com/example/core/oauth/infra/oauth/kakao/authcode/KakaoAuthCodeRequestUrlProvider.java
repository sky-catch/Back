package com.example.core.oauth.infra.oauth.kakao.authcode;

import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.authcode.AuthCodeRequestUrlProvider;
import com.example.core.oauth.infra.oauth.kakao.KakaoOauthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString(kakaoOauthConfig.getAuthorizeUrl())
                .queryParam("response_type", kakaoOauthConfig.getResponseType())
                .queryParam("client_id", kakaoOauthConfig.getClientId())
                .queryParam("redirect_uri", kakaoOauthConfig.getRedirectUri())
                .queryParam("scope", String.join(",", kakaoOauthConfig.getScope()))
                .toUriString();
    }
}
