package com.example.api.member;

import static com.example.core.oauth.domain.OauthServerType.KAKAO;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @BeforeEach
    void init() {
        memberMapper.deleteAll();
    }

    @Test
    @DisplayName("소셜 회원 저장 테스트")
    void test1() {
        // given
        OauthId oauthId = OauthId.builder()
                .oauthServerId(String.valueOf(1L))
                .oauthServerType(KAKAO)
                .build();
        MemberDTO expected = MemberDTO.builder()
                .nickname("testNickname")
                .profileImageUrl("testProfileImageUrl")
                .email("testEmail@test.com")
                .name("testName")
                .status(HumanStatus.ACTIVE)
                .oauthServerId(oauthId.oauthServerId())
                .oauthServer(oauthId.oauthServer())
                .build();

        // when
        memberMapper.save(expected);

        // then
        MemberDTO actual = memberMapper.findByOauthId(oauthId).get();
        assertAll(() -> {
            assertEquals(actual.getNickname(), expected.getNickname());
            assertEquals(actual.getProfileImageUrl(), expected.getProfileImageUrl());
            assertEquals(actual.getEmail(), expected.getEmail());
            assertEquals(actual.getName(), expected.getName());
            assertEquals(actual.getStatus(), expected.getStatus());
            assertEquals(actual.oauthId().oauthServerId(), expected.oauthId().oauthServerId());
            assertEquals(actual.oauthId().oauthServer(), expected.oauthId().oauthServer());
        });
    }
}