package com.twitter.ms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.twitter.ms.constants.ValidationContants.INVALID_EMAIL;
import static main.java.com.leon.baobui.constants.ErrorMessage.EMPTY_PASSWORD_CONFIRMATION;
import static main.java.com.leon.baobui.constants.ErrorMessage.SHORT_PASSWORD;

@Data
public class PasswordRegistrationRequest {
    @Email(regexp = ".+@.+\\..+", message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD_CONFIRMATION)
    @Size(min = 8, message = SHORT_PASSWORD)
    private String password;

}
