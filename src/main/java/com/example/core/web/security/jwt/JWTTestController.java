package com.example.core.web.security.jwt;

import com.example.api.member.MemberDTO;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트용 JWT")
public class JWTTestController {

    private final JWTProvider jwtProvider;

    @GetMapping("/oauth/jwt/test")
    @Operation(summary = "테스트용 JWT 발급", description = "test@test.com 회원의 JWT를 발급한다.")
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
    @Operation(summary = "테스트용 JWT 검증", description = "발급 받은 JWT로 회원의 이메일을 조회")
    public String authorizationTestJwt(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
                                       @Parameter(hidden = true) @LoginMember MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        return userDetails.getUsername() + " authenticated";
    }

    @Data
    @RequiredArgsConstructor
    public static class GetTestJWTResponse {
        private final String access_token;
    }
}
