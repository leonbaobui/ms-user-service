package com.twitter.ms.service;

import com.gmail.merikbest2015.dto.HeaderResponse;
import com.gmail.merikbest2015.dto.response.user.UserPrincipalResponse;
import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.ms.dto.request.UserRequest;
import com.twitter.ms.dto.response.AuthUserResponse;
import com.twitter.ms.dto.response.UserProfileResponse;
import com.twitter.ms.feign.ImageClient;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.BlockUserRepository;
import com.twitter.ms.repository.UserRepository;
import com.twitter.ms.repository.projection.AuthUserProjection;
import com.twitter.ms.repository.projection.UserPrincipalView;
import com.twitter.ms.repository.projection.UserProfileView;
import com.twitter.ms.repository.projection.UserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.gmail.merikbest2015.constants.ErrorMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BasicMapper basicMapper;
    private final UserRepository userRepository;
    private final ImageClient imageClient;

    @Transactional
    public void startUseTwitter() {
        Long userId = AuthUtil.getAuthenticatedUserId();
        userRepository.updateProfileStarted(userId);
    }

    public HeaderResponse<UserResponse> getUsers(Pageable pageable) {
        Long userId = AuthUtil.getAuthenticatedUserId();
        Page<UserProjection> allUserProjection = userRepository.findByActiveTrueAndIdNot(userId, pageable);
        return basicMapper.getHeaderResponse(allUserProjection, UserResponse.class);
    }

    public UserProfileResponse getUserProfileById(Long userId) {
        UserProfileView userProfileView = userRepository.getUserProfileById(userId)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return basicMapper.convertToResponse(userProfileView, UserProfileResponse.class);
    }

    public UserPrincipalResponse getUserByEmail(String email) {
        UserPrincipalView userPrincipalView = userRepository.getUserByEmail(email, UserPrincipalView.class)
                .orElseThrow(() -> new ApiRequestException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return basicMapper.convertToResponse(userPrincipalView, UserPrincipalResponse.class);
    }

    public String uploadImageUserProfile(MultipartFile multipartFile) {
        String s3ImageUrl = imageClient.uploadImageUserProfile(multipartFile);
        return s3ImageUrl;
    }

    @Transactional
    public AuthUserResponse updateUserProfile(UserRequest userRequest) {
        Long userId = AuthUtil.getAuthenticatedUserId();
        User user = userRepository.getUserById(userId, User.class)
                .orElseThrow(() -> new ApiRequestException("User not found!", HttpStatus.BAD_REQUEST));

        user.setFullName(userRequest.getFullName());
        user.setAbout(userRequest.getAbout());
        user.setLocation(userRequest.getLocation());
        user.setWebsite(userRequest.getWebsite());
        user.setProfileCustomized(true);

        if (userRequest.getAvatar() != null) {
            user.setAvatar(userRequest.getAvatar());
        }
        if (userRequest.getWallpaper() != null) {
            user.setWallpaper(userRequest.getWallpaper());
        }
        AuthUserProjection authUserProjection = userRepository.getUserById(userId, AuthUserProjection.class).get();
        return basicMapper.convertToResponse(authUserProjection, AuthUserResponse.class);
    }

    public List<UserResponse> searchUsersByUsername(String username, Pageable pageable) {
        Page<UserProjection> userProjectionPage = userRepository.getUserByUsername(username, pageable, UserProjection.class);
        return basicMapper.convertToResponseList(userProjectionPage.getContent(), UserResponse.class);
    }
}
