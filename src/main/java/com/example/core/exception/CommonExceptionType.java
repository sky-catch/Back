package com.example.core.exception;

import org.springframework.http.HttpStatus;

public interface CommonExceptionType {

    String getMessage();

    HttpStatus getHttpStatus();
}
