package com.example.core.oauth.domain;

import lombok.Builder;
import lombok.ToString;

@ToString
public class OauthMember {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String oauthServerId;
    private OauthServerType oauthServerType;

    public OauthMember(Long id, String nickname, String profileImageUrl, String oauthServerId,
                       OauthServerType oauthServerType) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.oauthServerId = oauthServerId;
        this.oauthServerType = oauthServerType;
    }

    @Builder
    public OauthMember(Long id, String nickname, String profileImageUrl, OauthId oauthId) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.oauthServerId = oauthId.oauthServerId();
        this.oauthServerType = oauthId.oauthServer();
    }

    public Long id() {
        return id;
    }

    public OauthId oauthId() {
        return new OauthId(oauthServerId, oauthServerType);
    }

    public String getOauthServerId() {
        return oauthServerId;
    }

    public OauthServerType getOauthServerType() {
        return oauthServerType;
    }

    public String nickname() {
        return nickname;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }
}