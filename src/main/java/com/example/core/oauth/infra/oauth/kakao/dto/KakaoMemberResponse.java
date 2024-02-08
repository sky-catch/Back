package com.example.core.oauth.infra.oauth.kakao.dto;

import com.example.core.oauth.domain.OauthId;
import com.example.core.oauth.domain.OauthMember;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.core.oauth.domain.OauthServerType.KAKAO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoMemberResponse {

    private Long id;
    private boolean hasSignedUp;
    private LocalDateTime connectedAt;
    private KakaoAccount kakaoAccount;

    public OauthMember toDomain() {
        return OauthMember.builder()
                .oauthId(new OauthId(String.valueOf(id), KAKAO))
                .nickname(kakaoAccount.profile.nickname)
                .profileImageUrl(kakaoAccount.profile.profileImageUrl)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class KakaoAccount {
        boolean profileNeedsAgreement;
        boolean profileNicknameNeedsAgreement;
        boolean profileImageNeedsAgreement;
        Profile profile;
        boolean nameNeedsAgreement;
        String name;
        boolean emailNeedsAgreement;
        boolean isEmailValid;
        boolean isEmailVerified;
        String email;
        boolean ageRangeNeedsAgreement;
        String ageRange;
        boolean birthyearNeedsAgreement;
        String birthyear;
        boolean birthdayNeedsAgreement;
        String birthday;
        String birthdayType;
        boolean genderNeedsAgreement;
        String gender;
        boolean phoneNumberNeedsAgreement;
        String phoneNumber;
        boolean ciNeedsAgreement;
        String ci;
        LocalDateTime ciAuthenticatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Profile {
        String nickname;
        String thumbnailImageUrl;
        String profileImageUrl;
        boolean isDefaultImage;
    }
}