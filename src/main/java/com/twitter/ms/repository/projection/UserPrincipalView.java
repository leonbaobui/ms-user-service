package com.twitter.ms.repository.projection;

public interface UserPrincipalView {
    Long getId();
    String getEmail();
    String getActivationCode();
}
