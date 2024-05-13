package com.example.core.web.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorResponse {

    private final int code;
    private final String message;

    @Builder
    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
