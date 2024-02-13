package com.example.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ExceptionResponse> SystemExceptionHandler(SystemException e) {

        String stackTrace = Arrays.toString(e.getStackTrace());

        log.error("SystemException : {} ,\n {}", e.getMessage() , stackTrace);

        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), stackTrace), e.getHttpStatus());
    }

}
