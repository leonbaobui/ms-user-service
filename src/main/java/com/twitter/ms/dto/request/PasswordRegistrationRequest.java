package com.twitter.ms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.gmail.merikbest2015.constants.ErrorMessage.EMPTY_PASSWORD_CONFIRMATION;
import static com.gmail.merikbest2015.constants.ErrorMessage.SHORT_PASSWORD;
import static com.twitter.ms.constants.ValidationContants.INVALID_EMAIL;

@Data
public class PasswordRegistrationRequest {
    @Email(regexp = ".+@.+\\..+", message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD_CONFIRMATION)
    @Size(min = 8, message = SHORT_PASSWORD)
    private String password;

}