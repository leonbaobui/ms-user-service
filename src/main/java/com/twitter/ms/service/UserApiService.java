package com.twitter.ms.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import com.twitter.ms.repository.BlockUserRepository;
import com.twitter.ms.repository.FollowerUserRepository;
import com.twitter.ms.repository.UserRepository;
import com.twitter.ms.repository.projection.ChatUserParticipantProjection;
import com.twitter.ms.repository.projection.NotificationUserProjection;
import com.twitter.ms.repository.projection.TweetAdditionalInfoUserProjection;
import com.twitter.ms.repository.projection.TweetAuthorProjection;
import com.twitter.ms.repository.projection.UserProjection;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import main.java.com.leon.baobui.dto.request.IdsRequest;
import main.java.com.leon.baobui.dto.response.chat.ChatUserParticipantResponse;
import main.java.com.leon.baobui.dto.response.notification.NotificationUserResponse;
import main.java.com.leon.baobui.dto.response.tweet.TweetAdditionalInfoUserResponse;
import main.java.com.leon.baobui.dto.response.tweet.TweetAuthorResponse;
import main.java.com.leon.baobui.dto.response.user.UserResponse;
import main.java.com.leon.baobui.exception.ApiRequestException;
import main.java.com.leon.baobui.mapper.BasicMapper;
import main.java.com.leon.baobui.util.AuthUtil;

import static main.java.com.leon.baobui.constants.ErrorMessage.CHAT_PARTICIPANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserApiService {
    private final BasicMapper basicMapper;
    private final UserRepository userRepository;
    private final BlockUserRepository blockUserRepository;
    private final FollowerUserRepository followerUserRepository;

    // Validate
    public boolean isUserExists(Long userId) {
        return userRepository.isUserExists(userId);
    }

    public boolean isUserHavePrivateProfile(Long userId, HttpServletRequest request) {
        Long authUserId = AuthUtil.getAuthenticatedUserId(request);
        return !userRepository.isUserHavePrivateProfile(userId, authUserId);
    }

    public boolean isMyProfileBlockedByUser(Long userId, HttpServletRequest request) {
        Long authUserId = AuthUtil.getAuthenticatedUserId(request);
        return blockUserRepository.isUserBlocked(userId, authUserId);
    }

    public boolean isUserFollowByOtherUser(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return followerUserRepository.isUserFollowByOtherUser(authUserId, userId);
    }

    public Long getUserPinnedTweetId(Long userId, HttpServletRequest request) {
        return userRepository.getPinnedTweetId(userId);
    }

    @Transactional
    public void updateMediaTweetCount(boolean increase) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        userRepository.updateMediaTweetCount(increase, authUserId);
    }

    @Transactional
    public void updateTweetCount(boolean increase) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        userRepository.updateTweetCount(increase, authUserId);
    }

    public List<Long> getValidUserIds(IdsRequest request) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        List<Long> blockedUserIds = userRepository.getUserIdsWhoBlockedMyProfile(request.getIds(), authUserId);
        request.getIds().removeAll(blockedUserIds);
        return userRepository.getValidUserIdsByIds(request.getIds(), authUserId);
    }

    public TweetAuthorResponse getTweetAuthor(Long userId) {
        TweetAuthorProjection authorProjection = userRepository.getUserById(userId, TweetAuthorProjection.class)
                .get();
        return basicMapper.convertToResponse(authorProjection, TweetAuthorResponse.class);
    }

    public TweetAdditionalInfoUserResponse getTweetAdditionalInfoUser(Long userId) {
        TweetAdditionalInfoUserProjection tweetAdditionalInfoUserProjection = userRepository
                .getUserById(userId, TweetAdditionalInfoUserProjection.class)
                .get();
        return basicMapper.convertToResponse(tweetAdditionalInfoUserProjection, TweetAdditionalInfoUserResponse.class);
    }

    @Transactional
    public void updatePinnedTweetId(Long tweetId) {
        Long userId = AuthUtil.getAuthenticatedUserId();
        Long pinnedTweetId = userRepository.getPinnedTweetId(userId);
        // For now, Twitter supports for only one tweet pinned. So if the below equal check correct,
        // we only need to set the pinned to null
        if (!ObjectUtils.isEmpty(pinnedTweetId) && pinnedTweetId.equals(tweetId)) {
            userRepository.updatePinnedTweetId(null, userId);
        }
    }

    public ChatUserParticipantResponse getChatUserParticipant(Long userId) {
        ChatUserParticipantProjection chatUserParticipantProjection =
                userRepository.getUserById(userId, ChatUserParticipantProjection.class).get();
        return basicMapper.convertToResponse(chatUserParticipantProjection, ChatUserParticipantResponse.class);
    }

    public UserResponse getUserResponseById(Long userId) {
        UserProjection userProjection = userRepository.getUserById(userId, UserProjection.class)
                .orElseThrow(() -> new ApiRequestException(CHAT_PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND));
        return basicMapper.convertToResponse(userProjection, UserResponse.class);
    }

    @Transactional
    public void updateLikeCount(boolean increaseCount) {
        Long userId = AuthUtil.getAuthenticatedUserId();
        userRepository.updateLikeCount(increaseCount, userId);
    }

    @Transactional
    public void increaseNotificationsCount(Long userId) {
        userRepository.increaseNotificationsCount(userId);
    }

    public NotificationUserResponse getNotificationUser(Long userId) {
        NotificationUserProjection user = userRepository.getUserById(userId, NotificationUserProjection.class).get();
        return basicMapper.convertToResponse(user, NotificationUserResponse.class);
    }
}
