package com.twitter.ms.service;

import com.gmail.merikbest2015.dto.HeaderResponse;
import com.gmail.merikbest2015.dto.request.NotificationRequest;
import com.gmail.merikbest2015.dto.response.notification.NotificationResponse;
import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.gmail.merikbest2015.enums.NotificationType;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.ms.exception.DataNotFoundException;
import com.twitter.ms.feign.NotificationClient;
import com.twitter.ms.model.User;
import com.twitter.ms.producer.FollowerUserProducer;
import com.twitter.ms.repository.FollowerUserRepository;
import com.twitter.ms.repository.UserRepository;
import com.twitter.ms.repository.projection.UserProjection;
import com.twitter.ms.service.helper.UserServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowerUserService {
//    private final NotificationClient notificationClient;
    private final FollowerUserRepository followerUserRepository;
    private final UserRepository userRepository;
    private final UserServiceHelper userServiceHelper;
    private final AuthenticationService authenticationService;
    private final FollowerUserProducer followerUserProducer;
    private final BasicMapper basicMapper;


    // Need transactional so hibernate can help auto-save
    // (update the follower list)
    @Transactional
    public boolean processFollow(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        User user = userRepository.getUserById(userId, User.class)
                .orElseThrow(() -> new DataNotFoundException("userId", "User not found", HttpStatus.NOT_FOUND));
        User authUser  = authenticationService.getAuthenticatedUser(authUserId);
        userServiceHelper.checkIsUserBlocked(user, authUser);
        boolean hasUserFollowed  = false;
        // Don't confuse between subscribers and follower!
        if (followerUserRepository.isFollower(authUserId, userId)) {
            authUser.getFollowers().remove(user);
            user.getSubscribers().remove(authUser);
        } else {
            if (!user.isPrivateProfile()) {
                authUser.getFollowers().add(user);
                NotificationRequest request = NotificationRequest.builder()
                        .notificationType(NotificationType.FOLLOW)
                        .userId(authUserId)
                        .userToFollowId(userId)
                        .notifiedUserId(userId)
                        .build();
//                notificationClient.sendNotification(request);
                hasUserFollowed  = true;
            } else {
                followerUserRepository.addFollowerRequest(authUserId, userId);
            }
        }
//        followerUserProducer.sendFollowUserEvent(user, authUser.getId(), hasUserFollowed);
        return hasUserFollowed;
    }

    public HeaderResponse<UserResponse> getFollowing(Long userId, Pageable pageable) {
        Page<UserProjection> userProjections  = followerUserRepository.getFollowingById(userId, pageable);
        return basicMapper.getHeaderResponse(userProjections, UserResponse.class) ;
    }

    public HeaderResponse<UserResponse> getFollowers(Long userId, Pageable pageable) {
        Page<UserProjection> userProjections  = followerUserRepository.getFollowersById(userId, pageable);
        return basicMapper.getHeaderResponse(userProjections, UserResponse.class) ;
    }

}
