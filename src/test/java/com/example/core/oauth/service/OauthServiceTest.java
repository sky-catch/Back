package com.example.core.oauth.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberMapper;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.core.oauth.domain.client.OauthMemberClient;
import com.example.core.oauth.domain.client.OauthMemberClientComposite;
import com.example.core.web.security.jwt.JWTProvider;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OauthServiceTest {

    private OauthService oauthService;

    @Autowired
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private JWTProvider jwtProvider;

    @BeforeEach
    void init() {
        OauthMemberClient client = new OauthMemberClient() {
            @Override
            public OauthServerType supportServer() {
                return OauthServerType.KAKAO;
            }

            @Override
            public MemberDTO fetch(String code) {
                return MemberDTO.builder()
                        .nickname("testNickname")
                        .profileImageUrl("testProfileImageUrl")
                        .email("test@test.com")
                        .name("testName")
                        .status(HumanStatus.ACTIVE)
                        .oauthServerId("1")
                        .oauthServerType(OauthServerType.KAKAO)
                        .build();
            }
        };
        Set<OauthMemberClient> clients = new HashSet<>();
        clients.add(client);
        OauthMemberClientComposite oauthMemberClientComposite = new OauthMemberClientComposite(clients);
        this.oauthService = new OauthService(authCodeRequestUrlProviderComposite, oauthMemberClientComposite,
                memberMapper, jwtProvider);
    }

    @Test
    @DisplayName("로그인 성공 시 accessToken 반환 테스트")
    void test1() {
        // given
        OauthServerType oauthServerType = OauthServerType.KAKAO;
        String authCode = "authCode";

        // when
        String actual = oauthService.login(oauthServerType, authCode);

        // then
        assertNotNull(actual);
    }
}