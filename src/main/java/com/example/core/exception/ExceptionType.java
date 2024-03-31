package com.example.core.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    String getMessage();

    HttpStatus getHttpStatus();
}
