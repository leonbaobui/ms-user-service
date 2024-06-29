package com.twitter.ms.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class RegistrationException extends RuntimeException {
    private String field;
    private String message;
    private HttpStatus httpStatus;
}
