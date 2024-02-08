package com.example.core.oauth.domain.mapper;

import static com.example.core.oauth.domain.OauthServerType.KAKAO;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.core.oauth.domain.OauthId;
import com.example.core.oauth.domain.OauthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OauthMemberMapperTest {

    @Autowired
    private OauthMemberMapper oauthMemberMapper;

    @Test
    @DisplayName("소셜 회원 저장 테스트")
    void test1() {
        // given
        OauthId oauthId = OauthId.builder()
                .oauthServerId(String.valueOf(1L))
                .oauthServerType(KAKAO)
                .build();
        OauthMember expected = OauthMember.builder()
                .nickname("testNickname")
                .profileImageUrl("testProfileImageUrl")
                .oauthId(oauthId)
                .build();

        // when
        oauthMemberMapper.save(expected);

        // then
        OauthMember actual = oauthMemberMapper.findByOauthId(oauthId).get();
        assertAll(() -> {
            assertEquals(actual.nickname(), expected.nickname());
            assertEquals(actual.profileImageUrl(), expected.profileImageUrl());
            assertEquals(actual.oauthId().oauthServer(), expected.oauthId().oauthServer());
            assertEquals(actual.oauthId().oauthServerId(), expected.oauthId().oauthServerId());
        });
    }
}