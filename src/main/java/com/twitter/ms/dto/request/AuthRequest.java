package com.twitter.ms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import static com.twitter.ms.constants.ValidationContants.INVALID_EMAIL;

@Data
@Builder
public class AuthRequest {

    @NotBlank
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{1,4}$", message = INVALID_EMAIL)
    private String email;
    @NotBlank
    @NotNull
    private String password;

}
