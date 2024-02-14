package com.example.api.member;

import com.example.core.dto.BaseDTO;
import com.example.core.oauth.domain.MemberStatus;
import com.example.core.oauth.domain.OauthId;
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
public class MemberDTO extends BaseDTO {

    private long memberId;
    private String nickname;
    private String profileImageUrl;
    private String email;
    private String name;
    private MemberStatus status;
    private String oauthServerId;
    private OauthServerType oauthServerType;

    public MemberDTO(Long memberId, String nickname, String profileImageUrl, String email, String name,
                     MemberStatus status, OauthId oauthId) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.name = name;
        this.status = status;
        this.oauthServerId = oauthId.oauthServerId();
        this.oauthServerType = oauthId.oauthServer();
    }

    public OauthId oauthId() {
        return new OauthId(oauthServerId, oauthServerType);
    }

    public Long memberId() {
        return memberId;
    }

    public String nickname() {
        return nickname;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }

    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    public MemberStatus status() {
        return status;
    }

    public String oauthServerId() {
        return oauthServerId;
    }

    public OauthServerType oauthServerType() {
        return oauthServerType;
    }

    public OauthServerType getOauthServer() {
        return oauthServerType;
    }

    public void setOauthServer(OauthServerType oauthServerType) {
        this.oauthServerType = oauthServerType;
    }
}