package com.example.core.web.security.jwt;

import com.example.api.member.MemberDTO;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.web.security.login.LoginMember;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JWTTestController {

    private final JWTProvider jwtProvider;

    // todo swagger 설정하기

    @GetMapping("/oauth/jwt/test")
    public GetTestJWTResponse createTestJWT() {
        MemberDTO testMember = MemberDTO.builder()
                .nickname("test nickname")
                .profileImageUrl("test profileImageUrl")
                .email("test@test.com")
                .name("test name")
                .status(HumanStatus.ACTIVE)
                .oauthServerId("1")
                .oauthServer(OauthServerType.KAKAO)
                .build();

        String accessToken = jwtProvider.createToken(testMember);
        return new GetTestJWTResponse(accessToken);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authorization/jwt/test")
    public String authorizationTestJwt(@AuthenticationPrincipal UserDetails userDetails,
                                       @LoginMember MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        return userDetails.getUsername() + " authenticated";
    }

    @Data
    @RequiredArgsConstructor
    public static class GetTestJWTResponse {
        private final String access_token;
    }
}
