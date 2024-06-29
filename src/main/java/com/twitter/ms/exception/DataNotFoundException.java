package com.twitter.ms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class DataNotFoundException extends RuntimeException {
    private String field;
    private String message;
    private HttpStatus httpStatus;
}
