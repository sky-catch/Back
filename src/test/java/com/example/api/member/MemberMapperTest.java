package com.example.api.member;

import static com.example.core.oauth.domain.OauthServerType.KAKAO;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.core.dto.HumanStatus;
import com.example.core.oauth.domain.OauthId;
import lombok.RequiredArgsConstructor;
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
class MemberMapperTest {

    private final MemberMapper memberMapper;

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