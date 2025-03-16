package com.twitter.ms.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.twitter.ms.enums.AuthenticationProvider;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraphUser {

    @Id
    private Long id;

    @Property(name = "about")
    private String about;

    @Property(name = "activation_code")
    private String activationCode;

    @Property(name = "active")
    private boolean active = false;

    @Property(name = "avatar")
    private String avatar;

    @Property(name = "background_color")
    private String backgroundColor = "DEFAULT";

    @Property(name = "color_scheme")
    private String colorScheme = "BLUE";

    @Property(name = "birthday")
    private String birthday;

    @Property(name = "country")
    private String country;

    @Property(name = "country_code")
    private String countryCode;

    @Property(name = "email")
    private String email;

    @Property(name = "full_name")
    private String fullName;

    @Property(name = "gender")
    private String gender;

    @Property(name = "language")
    private String language;

    @Property(name = "like_count")
    private Integer likeCount = 0;

    @Property(name = "location")
    private String location;

    @Property(name = "media_tweet_count")
    private Long mediaTweetCount = 0L;

    @Property(name = "muted_direct_messages")
    private boolean mutedDirectMessages = false;

    @Property(name = "notifications_count")
    private Long notificationsCount = 0L;

    @Property(name = "mentions_count")
    private Long mentionsCount = 0L;

    @Property(name = "password")
    private String password;

    @Property(name = "password_reset_code")
    private String passwordResetCode;

    @Property(name = "phone")
    private Long phone;

    @Property(name = "pinned_tweet_id")
    private Long pinnedTweetId;

    @Property(name = "private_profile")
    private boolean privateProfile = false;

    @Property(name = "profile_customized")
    private boolean profileCustomized = false;

    @Property(name = "profile_started")
    private boolean profileStarted = false;

    @Property(name = "registration_date")
    private ZonedDateTime registrationDate = ZonedDateTime.now();

    @Property(name = "role")
    private String role = "USER";

    @Property(name = "auth_provider")
    private AuthenticationProvider authenticationProvider;

    @Property(name = "tweet_count")
    private Long tweetCount = 0L;

    @Property(name = "unread_messages_count")
    private Long unreadMessagesCount = 0L;

    @Property(name = "username")
    private String username;

    @Property(name = "wallpaper")
    private String wallpaper;

    @Property(name = "website")
    private String website;

    @Property(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Property(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<GraphUser> following = new ArrayList<>();

}
