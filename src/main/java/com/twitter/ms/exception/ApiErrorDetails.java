package com.twitter.ms.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class ApiErrorDetails {
    private String field;
    private String value;
    private String issue;
    private String location;
}
