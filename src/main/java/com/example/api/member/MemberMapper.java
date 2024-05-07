package com.example.api.member;

import com.example.api.member.dto.MyMainDTO;
import com.example.api.member.dto.UpdateMemberDTO;
import com.example.core.oauth.domain.OauthId;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends UsersMapper<MemberDTO> {

    @Override
    Optional<MemberDTO> findByEmail(@Param("email") String email);

    Optional<MemberDTO> findByOauthId(OauthId oauthId);

    void save(MemberDTO memberDTO);

    void deleteAll();

    MemberDTO findById(long memberId);

    void updateMember(UpdateMemberDTO dto);

    Optional<MyMainDTO> findMyMainById(@Param("memberId") long memberId);
}