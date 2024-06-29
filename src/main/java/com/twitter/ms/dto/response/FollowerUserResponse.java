package com.twitter.ms.dto.response;

import lombok.Data;

@Data
public class FollowerUserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String about;
    private String avatar;
}