package com.example.core.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SystemException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public SystemException(String message) {
        this.message = message;
    }

    public SystemException(ExceptionType type) {
        this.message = type.getMessage();
        this.httpStatus = type.getHttpStatus();
    }
}
