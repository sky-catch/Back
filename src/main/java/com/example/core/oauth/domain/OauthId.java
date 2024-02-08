package com.example.core.oauth.domain;

import lombok.Builder;

public class OauthId {

    private String oauthServerId;

    private OauthServerType oauthServerType;

    @Builder
    public OauthId(String oauthServerId, OauthServerType oauthServerType) {
        this.oauthServerId = oauthServerId;
        this.oauthServerType = oauthServerType;
    }

    public String oauthServerId() {
        return oauthServerId;
    }

    public OauthServerType oauthServer() {
        return oauthServerType;
    }
}
