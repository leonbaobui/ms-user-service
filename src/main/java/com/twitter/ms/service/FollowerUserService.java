package com.twitter.ms.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import com.lib.twitter.lib_trans_outbox.domain.OutboxEvent;
import com.lib.twitter.lib_trans_outbox.service.TransactionOutboxService;
import com.twitter.ms.event.EventType;
import com.twitter.ms.event.UserSubscriptionEvent;
import com.twitter.ms.event.UserSubscriptionKey;
import com.twitter.ms.exception.DataNotFoundException;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.FollowerUserRepository;
import com.twitter.ms.repository.UserRepository;
import com.twitter.ms.repository.projection.UserProjection;
import com.twitter.ms.service.helper.UserServiceHelper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.com.leon.baobui.dto.HeaderResponse;
import main.java.com.leon.baobui.dto.request.NotificationRequest;
import main.java.com.leon.baobui.dto.response.user.UserResponse;
import main.java.com.leon.baobui.enums.NotificationType;
import main.java.com.leon.baobui.mapper.BasicMapper;
import main.java.com.leon.baobui.util.AuthUtil;

@Service
@RequiredArgsConstructor
public class FollowerUserService {
    private static final String TOPIC = "user-service.user.subscription-event";
    //    private final NotificationClient notificationClient;
    private final FollowerUserRepository followerUserRepository;
    private final UserRepository userRepository;
    private final UserServiceHelper userServiceHelper;
    private final AuthenticationService authenticationService;
//    private final FollowerUserProducer followerUserProducer;
    private final BasicMapper basicMapper;
    private final TransactionOutboxService transactionOutboxService;


    // Need transactional so hibernate can help auto-save
    // (update the follower list)
    @Transactional
    public boolean processFollow(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        User user = userRepository.getUserById(userId, User.class)
                .orElseThrow(() -> new DataNotFoundException("userId", "User not found", HttpStatus.NOT_FOUND));
        User authUser = authenticationService.getAuthenticatedUser(authUserId);
        userServiceHelper.checkIsUserBlocked(user, authUser);
        boolean hasUserFollowed = false;
        boolean isFollowing = false;

        if (followerUserRepository.isFollower(authUserId, userId)) {
            isFollowing = true;
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
                hasUserFollowed = true;
            } else {
                followerUserRepository.addFollowerRequest(authUserId, userId);
            }
        }

        var outboxEvent = OutboxEvent.<UserSubscriptionKey, UserSubscriptionEvent>builder()
                .eventType("created")
                .rootEntityType("user_subscriptions")
                .rootEntityId(String.valueOf(authUser.getId() + '-' + userId))
                .idempotencyKey(UUID.randomUUID().toString())
                .topic(TOPIC)
                .key(UserSubscriptionKey.newBuilder()
                        .setUserId(authUserId)
                        .setSubscriberId(userId)
                        .build()
                )
                .payload(UserSubscriptionEvent.newBuilder()
                        .setUserId(authUserId)
                        .setSubscriberId(userId)
                        .setEventType(isFollowing ? EventType.DELETED : EventType.CREATED)
                        .build())
                .build();
        transactionOutboxService.saveEventToOutboxTable(outboxEvent);
//        followerUserProducer.sendFollowUserEvent(user, authUser.getId(), hasUserFollowed);
        return hasUserFollowed;
    }

    public HeaderResponse<UserResponse> getFollowing(Long userId, Pageable pageable) {
        Page<UserProjection> userProjections = followerUserRepository.getFollowingById(userId, pageable);
        return basicMapper.getHeaderResponse(userProjections, UserResponse.class);
    }

    public HeaderResponse<UserResponse> getFollowers(Long userId, Pageable pageable) {
        Page<UserProjection> userProjections = followerUserRepository.getFollowersById(userId, pageable);
        return basicMapper.getHeaderResponse(userProjections, UserResponse.class);
    }

}
