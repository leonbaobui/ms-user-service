package com.twitter.ms.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
