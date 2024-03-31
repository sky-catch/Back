package com.example.api.member;

import com.example.api.reservation.ReservationDTO;
import com.example.core.dto.BaseDTO;
import com.example.core.dto.HumanStatus;
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

    // todo phone 추가하기
    private long memberId;
    private String nickname;
    private String profileImageUrl;
    private String email;
    private String name;
    private HumanStatus status;
    private String oauthServerId;
    private OauthServerType oauthServer;

    public MemberDTO(Long memberId, String nickname, String profileImageUrl, String email, String name,
                     HumanStatus status, OauthId oauthId) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.name = name;
        this.status = status;
        this.oauthServerId = oauthId.oauthServerId();
        this.oauthServer = oauthId.oauthServer();
    }

    public OauthId oauthId() {
        return new OauthId(oauthServerId, oauthServer);
    }

    public boolean isMine(ReservationDTO reservation) {
        return this.memberId == reservation.getMemberId();
    }
}