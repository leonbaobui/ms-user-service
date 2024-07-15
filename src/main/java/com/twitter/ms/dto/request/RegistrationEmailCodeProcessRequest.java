package com.twitter.ms.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;

import static com.twitter.ms.constants.ValidationContants.INVALID_EMAIL;

@Data
@Getter
@Setter
public class RegistrationEmailCodeProcessRequest {
    @Email(regexp = ".+@.+\\..+", message = INVALID_EMAIL)
    private String email;
}
