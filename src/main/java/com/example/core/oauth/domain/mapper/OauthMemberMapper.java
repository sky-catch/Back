package com.example.core.oauth.domain.mapper;

import com.example.core.oauth.domain.OauthId;
import com.example.core.oauth.domain.OauthMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface OauthMemberMapper {

    Optional<OauthMember> findByOauthId(OauthId oauthId);

    void save(OauthMember oauthMember);
}
