package com.twitter.ms.repository.projection;

import java.time.LocalDateTime;

public interface UserProfileView {
    Long getId();
    String getFullName();
    String getUsername();
    String getLocation();
    String getAbout();
    String getWebsite();
    String getCountry();
    String getBirthday();
    LocalDateTime getRegistrationDate();
    Long getTweetCount();
    Long getMediaTweetCount();
    Long getLikeCount();
    boolean isMutedDirectMessages();
    boolean isPrivateProfile();
    String getAvatar();
    String getWallpaper();
    Long getPinnedTweetId();

}
