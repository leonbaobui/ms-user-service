package com.twitter.ms.mapper;

import com.twitter.ms.dto.request.RegistrationRequest;
import com.twitter.ms.dto.response.AuthUserResponse;
import com.twitter.ms.event.UserEvent;
import com.twitter.ms.model.User;
import com.twitter.ms.security.oauth2.GoogleOAuth2User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "fullName", source = "request.username")
    User registrationRequestToUserDAO(RegistrationRequest request);

    @Mapping(target = "username", source = "googleOAuth2User.name")
    @Mapping(target = "avatar", source = "googleOAuth2User.avatarUrl")
    @Mapping(target = "email", source = "googleOAuth2User.email")
    @Mapping(target = "fullName", source = "googleOAuth2User.name")
    @Mapping(target = "active", expression = "java(true)")
    User oauth2RegistrationRequestToUserDAO(GoogleOAuth2User googleOAuth2User);

    UserEvent userEntityToUserEventDTO(User user);
    AuthUserResponse userEntityToAuthUserDTO(User user);
}
