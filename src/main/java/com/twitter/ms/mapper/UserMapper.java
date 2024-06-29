package com.twitter.ms.mapper;

import com.twitter.ms.dto.request.RegistrationRequest;
import com.twitter.ms.dto.response.AuthUserResponse;
import com.twitter.ms.dto.response.UserProfileResponse;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.projection.UserProfileView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    @Mapping(target = "fullName", source =  "request.username")
    User registrationRequestToUserDAO(RegistrationRequest request);
    AuthUserResponse userEntityToAuthUserDTO(User user);
}
