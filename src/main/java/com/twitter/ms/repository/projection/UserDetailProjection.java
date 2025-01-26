package com.twitter.ms.repository.projection;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public interface UserDetailProjection {
    Long getId();
    String getFullName();
    String getUsername();
    String getAbout();
    String getAvatar();
    boolean getPrivateProfile();
    Long getFollowersCount();
    Long getFollowingCount();

    @Value("#{@userServiceHelper.isUserBlockedByMyProfile(target.id)}")
    boolean getIsUserBlocked();

    @Value("#{@userServiceHelper.isMyProfileBlockedByUser(target.id)}")
    boolean getIsMyProfileBlocked();

    @Value("#{@userServiceHelper.isMyProfileWaitingForApprove(target.id)}")
    boolean getIsWaitingForApprove();

    @Value("#{@userServiceHelper.isUserFollowByOtherUser(target.id)}")
    boolean getIsFollower();

    @Value("#{@userServiceHelper.getSameFollowers(target.id)}")
    List<SameFollower> getSameFollowers();
}

