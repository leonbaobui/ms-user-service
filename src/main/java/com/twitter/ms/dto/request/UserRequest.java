package com.twitter.ms.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static main.java.com.leon.baobui.constants.ErrorMessage.INCORRECT_USERNAME_LENGTH;

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
