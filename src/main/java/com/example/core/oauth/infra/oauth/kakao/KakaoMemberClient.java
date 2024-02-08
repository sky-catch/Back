package com.example.core.oauth.infra.oauth.kakao;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.example.core.oauth.domain.OauthMember;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.client.OauthMemberClient;
import com.example.core.oauth.infra.oauth.kakao.dto.KakaoMemberResponse;
import com.example.core.oauth.infra.oauth.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    // todo fetch refactoring
    @Override
    public OauthMember fetch(String authCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(tokenRequestParams(authCode),
                httpHeaders);
        RestTemplate restTemplate = new RestTemplate();

        KakaoToken tokenInfo = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                POST,
                httpEntity,
                KakaoToken.class
        ).getBody();

        if (tokenInfo == null) {
            throw new NullPointerException("KakaoToken is null");
        }

        httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + tokenInfo.getAccessToken());
        httpEntity = new HttpEntity<>(httpHeaders);
        restTemplate = new RestTemplate();

        KakaoMemberResponse kakaoMemberResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                GET,
                httpEntity,
                KakaoMemberResponse.class
        ).getBody();

        if (kakaoMemberResponse == null) {
            throw new NullPointerException("kakaoMemberResponse is null");
        }

        return kakaoMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.getClientId());
        params.add("redirect_uri", kakaoOauthConfig.getRedirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.getClientSecret());
        System.out.println("params = " + params);
        return params;
    }
}
