package com.example.core.oauth.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberMapper;
import com.example.api.owner.OwnerMapper;
import com.example.api.owner.dto.Owner;
import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.core.oauth.domain.client.OauthMemberClient;
import com.example.core.oauth.domain.client.OauthMemberClientComposite;
import com.example.core.oauth.dto.LoginResponse;
import com.example.core.web.security.jwt.JWTProvider;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
@ActiveProfiles("test")
@Sql("classpath:truncate.sql")
class OauthServiceTest {

    private OauthService oauthService;

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final MemberMapper memberMapper;
    private final OwnerMapper ownerMapper;
    private final JWTProvider jwtProvider;

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
                        .oauthServer(OauthServerType.KAKAO)
                        .build();
            }
        };
        Set<OauthMemberClient> clients = new HashSet<>();
        clients.add(client);
        OauthMemberClientComposite oauthMemberClientComposite = new OauthMemberClientComposite(clients);
        this.oauthService = new OauthService(authCodeRequestUrlProviderComposite, oauthMemberClientComposite,
                memberMapper, ownerMapper, jwtProvider);
    }

    @Test
    @DisplayName("회원 로그인 성공 시 accessToken, isOwner = false 반환 테스트")
    void test1() {
        // given
        OauthServerType oauthServerType = OauthServerType.KAKAO;
        String authCode = "authCode";

        // when
        LoginResponse actual = oauthService.login(oauthServerType, authCode);

        // then
        assertNotNull(actual.getAccessToken());
        assertFalse(actual.getUsersDTO().isOwner());
    }

    @Test
    @DisplayName("사장 로그인 성공 시 accessToken, isOwner = true 반환 테스트")
    void test2() {
        // given
        Owner owner = Owner.builder()
                .name("test owner")
                .email("test@test.com")
                .oauthServer(OauthServerType.KAKAO)
                .status(HumanStatus.ACTIVE)
                .businessRegistrationNumber("101-01-00015")
                .build();
        ownerMapper.createOwner(owner);

        OauthServerType oauthServerType = OauthServerType.KAKAO;
        String authCode = "authCode";

        // when
        LoginResponse actual = oauthService.login(oauthServerType, authCode);

        // then
        assertNotNull(actual.getAccessToken());
        assertTrue(actual.getUsersDTO().isOwner());
    }
}