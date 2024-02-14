package com.example.core.web.security.jwt;

import com.example.api.member.MemberDTO;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JWTTestController {

    private final JWTProvider jwtProvider;

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

    @GetMapping("/authorization/jwt/test")
    public String authorizationTestJwt(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername() + " authenticated";
    }

    @Data
    @RequiredArgsConstructor
    public static class GetTestJWTResponse {
        private final String access_token;
    }
}
