package com.twitter.ms.controller;

import com.twitter.ms.exception.RegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import main.java.com.leon.baobui.dto.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {RegistrationException.class})
    public ResponseEntity<CommonResponse> handleRegistrationException(Exception exception) {
        return new ResponseEntity<>(
                CommonResponse.builder()
                        .message(exception.getMessage())
                        .httpStatus("500 Internal server error")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<CommonResponse> exceptionHandler(Exception exception) {
//        return new ResponseEntity<>(
//                CommonResponse.builder()
//                .message(exception.getMessage())
//                .httpStatus("500 Internal server error")
//                .build(),
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
