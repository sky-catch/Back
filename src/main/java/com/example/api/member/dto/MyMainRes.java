package com.example.api.member.dto;

import com.example.core.dto.HumanStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyMainRes {

    private String nickname;
    private String profileImageUrl;
    private String name;
    private HumanStatus status;

    @Builder
    public MyMainRes(String nickname, String profileImageUrl, String name, HumanStatus status) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.status = status;
    }
}
