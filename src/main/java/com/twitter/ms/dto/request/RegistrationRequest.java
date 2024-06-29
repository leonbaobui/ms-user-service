package com.twitter.ms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.twitter.ms.constants.ValidationContants.*;

@Data
@Getter
@Setter
public class RegistrationRequest {
    @Email(regexp = ".+@.+\\..+", message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = INVALID_NAME_BLANK_MSG)
    @Size(min = 1, max = 50, message = INVALID_NAME_SIZE_MSG)
    private String username;

    private String birthday;
}
