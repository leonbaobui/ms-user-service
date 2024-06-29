package com.twitter.ms.repository.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

public interface ChatUserParticipantProjection {

    public Long getId();
    public String getFullName();

    public String getUsername();
    public String getAvatar();
    public boolean getMutedDirectMessages();
    @Value("#{@userServiceHelper.isUserBlockedByMyProfile(target.id)}")
    public boolean getIsUserBlocked();
    @Value("#{@userServiceHelper.isMyProfileBlockedByUser(target.id)}")
    public boolean getIsMyProfileBlocked();

}
