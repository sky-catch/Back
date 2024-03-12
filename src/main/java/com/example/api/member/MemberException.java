package com.example.api.member;

import lombok.Getter;

@Getter
public enum MemberException {
    NOT_FOUND("존재하지 않는 사용자입니다.");

    private final String message;

    MemberException(String message) {
        this.message = message;
    }
}
