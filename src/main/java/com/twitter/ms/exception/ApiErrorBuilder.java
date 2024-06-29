package com.twitter.ms.exception;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Builder
@UtilityClass
public class ApiErrorBuilder {
    public static ResponseEntity<ApiError> buildApiResponse(Exception exception, HttpStatus status) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .details(buildDetailsResponseFactoryMethod(exception, status))
                        .build(), HttpStatus.BAD_REQUEST);
    }

    private static List<ApiErrorDetails> buildDetailsResponseFactoryMethod(Exception exception, HttpStatus status) {
        if (exception instanceof RegistrationException) {
            return buildRegistrationExceptionDetails((RegistrationException) exception);
        }
        return null;
    }

    private List<ApiErrorDetails> buildRegistrationExceptionDetails(RegistrationException e) {
        return List.of(ApiErrorDetails.builder()
                .field(e.getField())
                .value(e.getMessage())
                .build());
    }
}
