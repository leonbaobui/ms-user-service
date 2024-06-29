package com.twitter.ms.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.gmail.merikbest2015.constants.ErrorMessage.INCORRECT_USERNAME_LENGTH;

@Data
public class UserRequest {
    @Size(min = 0, max = 50, message = INCORRECT_USERNAME_LENGTH)
    private String fullName;
    private String about;
    private String location;
    private String website;
    @Nullable
    private String avatar;
    @Nullable
    private String wallpaper;
}
