package com.example.core.oauth.domain;

import static java.util.Locale.ENGLISH;

import lombok.Getter;

@Getter
public enum OauthServerType {
    KAKAO("카카오"),
    ;

    private final String value;

    OauthServerType(String value) {
        this.value = value;
    }

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}
