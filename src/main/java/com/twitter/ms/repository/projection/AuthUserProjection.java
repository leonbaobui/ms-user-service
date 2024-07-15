package com.twitter.ms.repository.projection;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import main.java.com.leon.baobui.enums.BackgroundColorType;
import main.java.com.leon.baobui.enums.ColorSchemeType;

public interface AuthUserProjection {
    Long getId();

    String getEmail();

    String getFullName();

    String getUsername();

    String getLocation();

    String getAbout();

    String getWebsite();

    String getCountryCode();

    Long getPhone();

    String getCountry();

    String getGender();

    String getLanguage();

    String getBirthday();

    LocalDateTime getRegistrationDate();

    Long getTweetCount();

    Long getMediaTweetCount();

    Long getLikeCount();

    Long getNotificationsCount();

    Long getMentionsCount();

    boolean isActive();

    boolean isProfileCustomized();

    boolean isProfileStarted();

    boolean isMutedDirectMessages();

    boolean isPrivateProfile();

    BackgroundColorType getBackgroundColor();

    ColorSchemeType getColorScheme();

    String getAvatar();

    String getWallpaper();

    Long getPinnedTweetId();

    Long getUnreadMessagesCount();

    @Value("#{target.followers.size()}")
    Long getFollowersSize();

    @Value("#{target.following.size()}")
    Long getFollowingSize();

    @Value("#{target.followerRequests.size()}")
    Long getFollowerRequestsSize();
}
