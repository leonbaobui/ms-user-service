package com.twitter.ms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.twitter.ms.enums.AuthenticationProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @Column(name = "about")
    private String about;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "active")
    private boolean active = false;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "background_color")
    private String backgroundColor = "DEFAULT";

    @Column(name = "color_scheme")
    private String colorScheme = "BLUE";

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "country")
    private String country;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "language")
    private String language;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "location")
    private String location;

    @Column(name = "media_tweet_count", columnDefinition = "int8 default 0")
    private Long mediaTweetCount = 0L;

    @Column(name = "muted_direct_messages", columnDefinition = "boolean default false")
    private boolean mutedDirectMessages = false;

    @Column(name = "notifications_count", columnDefinition = "int8 default 0")
    private Long notificationsCount = 0L;

    @Column(name = "mentions_count", columnDefinition = "int8 default 0")
    private Long mentionsCount = 0L;

    @Column(name = "password")
    private String password;

    @Column(name = "password_reset_code")
    private String passwordResetCode;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "pinned_tweet_id")
    private Long pinnedTweetId;

    @Column(name = "private_profile", columnDefinition = "boolean default false")
    private boolean privateProfile = false;

    @Column(name = "profile_customized", columnDefinition = "boolean default false")
    private boolean profileCustomized = false;

    @Column(name = "profile_started", columnDefinition = "boolean default false")
    private boolean profileStarted = false;

    @Column(name = "registration_date", columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "role", columnDefinition = "varchar(255) default 'USER'")
    private String role = "USER";

    @Enumerated(value = EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authenticationProvider;

    @Column(name = "tweet_count", columnDefinition = "int8 default 0")
    private Long tweetCount = 0L;

    @Column(name = "unread_messages_count", columnDefinition = "int8 default 0")
    private Long unreadMessagesCount = 0L;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "wallpaper")
    private String wallpaper;

    @Column(name = "website")
    private String website;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    List<User> followers;

    @ManyToMany
    @JoinTable(name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> following;

    @ManyToMany
    @JoinTable(name = "user_muted",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "muted_user_id"))
    private List<User> userMutedList;

    @ManyToMany
    @JoinTable(name = "user_blocked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_user_id"))
    private List<User> userBlockedList;

    @ManyToMany
    @JoinTable(name = "user_follower_requests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> followerRequests;

    @ManyToMany
    @JoinTable(name = "subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private List<User> subscribers = new ArrayList<>();

}
