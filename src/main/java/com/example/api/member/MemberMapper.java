package com.example.api.member;

import com.example.api.member.dto.MyMainDTO;
import com.example.api.member.dto.UpdateMemberDTO;
import com.example.api.savedrestaurant.SavedRestaurantDTO;
import com.example.core.oauth.domain.OauthId;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends UsersMapper<MemberDTO> {

    @Override
    Optional<MemberDTO> findByEmail(@Param("email") String email);

    Optional<MemberDTO> findByOauthId(OauthId oauthId);

    void save(MemberDTO memberDTO);

    MemberDTO findById(long memberId);

    void updateMember(UpdateMemberDTO dto);

    Optional<MyMainDTO> findMyMainById(@Param("memberId") long memberId);

    List<SavedRestaurantDTO> findSavedRestaurant(long memberId);

}