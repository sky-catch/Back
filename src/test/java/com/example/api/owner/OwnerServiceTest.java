package com.example.api.owner;

import com.example.api.member.MemberDTO;
import com.example.core.dto.HumanStatus;
import com.example.core.exception.SystemException;
import com.example.core.oauth.domain.OauthId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.example.core.oauth.domain.OauthServerType.KAKAO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:truncate.sql")
class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private OwnerMapper ownerMapper;

    @BeforeEach
    @AfterEach
    void init() {
        ownerMapper.deleteAll();
    }

    @Test
    @DisplayName("새로운 사장을 생성하는 테스트")
    void test1() {
        // given
        OauthId oauthId = OauthId.builder()
                .oauthServerId(String.valueOf(1L))
                .oauthServerType(KAKAO)
                .build();
        MemberDTO dto = MemberDTO.builder()
                .nickname("testNickname")
                .profileImageUrl("testProfileImageUrl")
                .email("testEmail@test.com")
                .name("testName")
                .status(HumanStatus.ACTIVE)
                .oauthServerId(oauthId.oauthServerId())
                .oauthServer(oauthId.oauthServer())
                .build();

        // when
        long before = ownerMapper.findAll().size();
        ownerService.createOwner(dto, "101-01-00015");
        long actual = ownerMapper.findAll().size();

        // then
        assertEquals(before + 1, actual);
    }

    @Test
    @DisplayName("중복 사장 생성은 예외를 반환하는 테스트")
    void test2() {
        // given
        OauthId oauthId = OauthId.builder()
                .oauthServerId(String.valueOf(1L))
                .oauthServerType(KAKAO)
                .build();
        MemberDTO dto = MemberDTO.builder()
                .nickname("testNickname")
                .profileImageUrl("testProfileImageUrl")
                .email("testEmail@test.com")
                .name("testName")
                .status(HumanStatus.ACTIVE)
                .oauthServerId(oauthId.oauthServerId())
                .oauthServer(oauthId.oauthServer())
                .build();
        ownerService.createOwner(dto, "101-01-00015");

        // expected
        assertThatThrownBy(() -> ownerService.createOwner(dto, "101-01-00015"))
                .isInstanceOf(SystemException.class)
                .hasMessageContaining("사장의 중복 생성은 불가능합니다.");
    }
}