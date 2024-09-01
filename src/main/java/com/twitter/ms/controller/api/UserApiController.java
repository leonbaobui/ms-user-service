package com.twitter.ms.controller.api;

import java.util.List;

import lombok.RequiredArgsConstructor;
import com.twitter.ms.service.UserApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import main.java.com.leon.baobui.dto.request.IdsRequest;
import main.java.com.leon.baobui.dto.response.chat.ChatUserParticipantResponse;
import main.java.com.leon.baobui.dto.response.notification.NotificationUserResponse;
import main.java.com.leon.baobui.dto.response.tweet.TweetAdditionalInfoUserResponse;
import main.java.com.leon.baobui.dto.response.tweet.TweetAuthorResponse;
import main.java.com.leon.baobui.dto.response.user.UserResponse;

import static main.java.com.leon.baobui.constants.PathConstants.API_V1_USER;
import static main.java.com.leon.baobui.constants.PathConstants.CHAT_PARTICIPANT_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.IS_EXISTS_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.IS_FOLLOWED_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.IS_MY_PROFILE_BLOCKED_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.IS_PRIVATE_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.LIKE_COUNT;
import static main.java.com.leon.baobui.constants.PathConstants.MEDIA_COUNT;
import static main.java.com.leon.baobui.constants.PathConstants.NOTIFICATION_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.NOTIFICATION_USER_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.TWEET_ADDITIONAL_INFO_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.TWEET_AUTHOR_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.TWEET_COUNT;
import static main.java.com.leon.baobui.constants.PathConstants.TWEET_PINNED_TWEET_ID;
import static main.java.com.leon.baobui.constants.PathConstants.TWEET_PINNED_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.VALID_IDS;

@RestController
@RequestMapping(value = API_V1_USER)
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userService;

    @GetMapping(IS_EXISTS_USER_ID)
    public Boolean isUserExist(@PathVariable(name = "userId") Long userId) {
        return userService.isUserExists(userId);
    }

    @GetMapping(IS_PRIVATE_USER_ID)
    public Boolean isUserHavePrivateProfile(@PathVariable(name = "userId") Long userId,
                                            HttpServletRequest request) {
        return userService.isUserHavePrivateProfile(userId, request);
    }

    @GetMapping(IS_MY_PROFILE_BLOCKED_USER_ID)
    public Boolean isMyProfileBlockedByUser(@PathVariable("userId") Long userId,
                                            HttpServletRequest request) {
        return userService.isMyProfileBlockedByUser(userId, request);
    }

    @GetMapping(TWEET_PINNED_USER_ID)
    public Long getUserPinnedTweetId(@PathVariable("userId") Long userId, HttpServletRequest request) {
        return userService.getUserPinnedTweetId(userId, request);
    }

    @GetMapping(IS_FOLLOWED_USER_ID)
    public Boolean isUserFollowByOtherUser(@PathVariable("userId") Long userId) {
        return userService.isUserFollowByOtherUser(userId);
    }

    @PutMapping(MEDIA_COUNT)
    public void updateMediaTweetCount(@PathVariable("increaseCount") boolean increaseCount) {
        userService.updateMediaTweetCount(increaseCount);
    }

    @PutMapping(TWEET_COUNT)
    public void updateTweetCount(@PathVariable("increaseCount") boolean increaseCount) {
        userService.updateTweetCount(increaseCount);
    }

    @GetMapping(TWEET_AUTHOR_USER_ID)
    public TweetAuthorResponse getTweetAuthor(@PathVariable("userId") Long userId) {
        return userService.getTweetAuthor(userId);
    }

    @GetMapping(TWEET_ADDITIONAL_INFO_USER_ID)
    public TweetAdditionalInfoUserResponse getTweetAdditionalInfoUser(@PathVariable("userId") Long userId) {
        return userService.getTweetAdditionalInfoUser(userId);
    }

    @PutMapping(TWEET_PINNED_TWEET_ID)
    public void updatePinnedTweetId(@PathVariable("tweetId") Long tweetId) {
        userService.updatePinnedTweetId(tweetId);
    }

    @PostMapping(VALID_IDS)
    public List<Long> getValidUserIds(@RequestBody IdsRequest request) {
        return userService.getValidUserIds(request);
    }

    @GetMapping(CHAT_PARTICIPANT_USER_ID)
    public ChatUserParticipantResponse getChatUserParticipant(@PathVariable("userId") Long userId) {
        return userService.getChatUserParticipant(userId);
    }

    @GetMapping(USER_ID)
    public UserResponse getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserResponseById(userId);
    }

    @PutMapping(LIKE_COUNT)
    public void updateLikeCount(@PathVariable("increaseCount") boolean increaseCount) {
        userService.updateLikeCount(increaseCount);
    }

    @GetMapping(NOTIFICATION_USER_ID)
    public void increaseNotificationsCount(@PathVariable("userId") Long userId) {
        userService.increaseNotificationsCount(userId);
    }

    @GetMapping(NOTIFICATION_USER_USER_ID)
    public NotificationUserResponse getNotificationUser(@PathVariable("userId") Long userId) {
        return userService.getNotificationUser(userId);
    }
}
