package com.example.core.web.security.jwt.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessToken {
    private String value;

    @Builder
    public AccessToken(String value) {
        this.value = value;
    }
}
