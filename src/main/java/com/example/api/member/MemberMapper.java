package com.example.api.member;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    Optional<MemberDTO> findByEmail(@Param("email") String email);

    void save(MemberDTO memberDTO);
}