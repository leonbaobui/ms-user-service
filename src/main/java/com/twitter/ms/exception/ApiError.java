package com.twitter.ms.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class ApiError {
    private String errorId;
    private String message;
    private List<ApiErrorDetails> details;
    private HttpStatus httpStatus;
}
