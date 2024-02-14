package com.example.api.member;

import com.example.core.oauth.domain.OauthId;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    Optional<MemberDTO> findByEmail(@Param("email") String email);

    Optional<MemberDTO> findByOauthId(OauthId oauthId);

    void save(MemberDTO memberDTO);

    void deleteAll();
}