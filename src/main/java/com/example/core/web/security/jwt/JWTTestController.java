package com.example.core.web.security.jwt;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberService;
import com.example.api.owner.OwnerService;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.dto.LoginResponse;
import com.example.core.web.security.dto.UsersDTO;
import com.example.core.web.security.jwt.dto.AccessToken;
import com.example.core.web.security.login.LoginMember;
import com.example.core.web.security.login.LoginOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트용 JWT")
public class JWTTestController {

    private final JWTProvider jwtProvider;
    private final MemberService memberService;
    private final OwnerService ownerService;

    @GetMapping("/oauth/jwt/test")
    @Operation(summary = "회원용 테스트 JWT 발급", description = "test@test.com 회원의 JWT를 발급한다.")
    public ResponseEntity<LoginResponse> createTestJWT() {
        MemberDTO testMember = MemberDTO.builder()
                .nickname("test nickname")
                .profileImageUrl("test profileImageUrl")
                .email("test@test.com")
                .name("test name")
                .status(HumanStatus.ACTIVE)
                .oauthServerId("1")
                .oauthServer(OauthServerType.KAKAO)
                .build();

        UsersDTO usersDTO = UsersDTO.of(testMember);

        AccessToken accessToken = jwtProvider.createToken(usersDTO);

        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .isOwner(false)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth/jwt/test/{memberId}")
    @Operation(summary = "회원용 테스트 JWT 발급 - memberId 입력")
    public ResponseEntity<LoginResponse> createTestJWTById(@PathVariable long memberId) {
        MemberDTO testMember = memberService.getMemberById(memberId);

        UsersDTO usersDTO = UsersDTO.of(testMember);

        AccessToken accessToken = jwtProvider.createToken(usersDTO);
        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .isOwner(false)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth/jwt/test/owner/{ownerId}")
    @Operation(summary = "사장용 테스트 JWT 발급 - ownerId 입력")
    public ResponseEntity<LoginResponse> createTestOwnerJWTById(@PathVariable long ownerId) {
        GetOwnerRes testOwner = ownerService.getOwner(ownerId);

        UsersDTO usersDTO = UsersDTO.of(testOwner);

        AccessToken accessToken = jwtProvider.createToken(usersDTO);
        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .isOwner(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/authorization/jwt/member/test")
    @Operation(summary = "회원 테스트용 JWT 검증", description = "발급 받은 JWT로 회원의 이메일을 조회")
    public String authorizationTestMemberJwt(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
                                             @Parameter(hidden = true) @LoginMember MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        return userDetails.getUsername() + " authenticated";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/authorization/jwt/owner/test")
    @Operation(summary = "사장 테스트용 JWT 검증", description = "발급 받은 JWT로 회원의 이메일을 조회")
    public String authorizationTestOwnerJwt(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
                                            @Parameter(hidden = true) @LoginOwner Owner owner) {
        System.out.println("owner = " + owner);
        return userDetails.getUsername() + " authenticated";
    }
}
