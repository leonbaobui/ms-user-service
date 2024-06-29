package com.twitter.ms.repository;

import com.gmail.merikbest2015.dto.response.chat.ChatUserParticipantResponse;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.projection.ChatUserParticipantProjection;
import com.twitter.ms.repository.projection.UserProfileView;
import com.twitter.ms.repository.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //QUERY
    Optional<User> findByEmail(String email);
    <T> Optional<T> getUserById(Long userId, Class<T> projections);

    <T> Optional<T> getUserByEmail(String email, Class<T> projections);

    @Query("SELECT user FROM User user " +
            "WHERE (UPPER(user.username) LIKE UPPER(CONCAT('%',:username,'%')) AND user.active = true) " +
            "OR (UPPER(user.fullName) LIKE UPPER(CONCAT('%',:username,'%')) AND user.active = true)")
    <T> Page<T> getUserByUsername(@Param("username") String username, Pageable pageable, Class<T> projections);

    Optional<UserProfileView> getUserProfileById(Long id);

    @Query("SELECT u.activationCode FROM User u WHERE u.id = :userId")
    String findActivationCodeByUserId(@Param("userId") Long userId);

    @Query("SELECT " +
            "CASE " +
                "WHEN count(user) > 0 THEN true " +
                "ELSE false " +
            "END " +
            "FROM User user " +
            "WHERE user.id = :userId")
    Boolean isUserExists(@Param("userId") Long userId);

    @Query("SELECT user.privateProfile FROM User user WHERE user.id = :userId")
    Boolean isPrivateProfile(@Param("userId") Long userId);

    @Query("SELECT CASE " +
            "WHEN count(user) > 0 THEN true ELSE false " +
            "END " +
            "FROM User user " +
            "LEFT JOIN user.following following " +
            "WHERE user.id = :userId AND user.privateProfile = false " +
            "OR user.id = :userId AND user.privateProfile = true AND following.id = :authUserId")
    Boolean isUserHavePrivateProfile(@Param("userId") Long userId, @Param("authUserId") Long authUserId);

    @Query("SELECT CASE WHEN count(followerRequest) > 0 THEN true ELSE false END FROM User user " +
            "LEFT JOIN user.followerRequests followerRequest " +
            "WHERE user.id = :userId " +
            "AND followerRequest.id = :authUserId")
    boolean isMyProfileWaitingForApprove(@Param("userId") Long userId, @Param("authUserId") Long authUserId);

    Page<UserProjection> findByActiveTrueAndIdNot(Long userId, Pageable pageable);

    @Query("SELECT user.pinnedTweetId FROM User user WHERE user.id = :userId")
    Long getPinnedTweetId(@Param("userId") Long userId);

    //UPDATE
    @Modifying
    @Query("UPDATE User user SET user.activationCode = :activationCode WHERE user.id = :userId")
    void updateActivationCode(@Param("activationCode") String activationCode, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User user SET user.active = :active WHERE user.id = :userId")
    void updateAccountStatus(@Param("active") Boolean active, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User user SET user.password = :password WHERE user.id = :userId")
    void updatePassword(@Param("password") String password, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User user SET user.tweetCount = " +
            "CASE WHEN :increaseCount = true THEN (user.tweetCount + 1) " +
            "ELSE (user.tweetCount - 1) END " +
            "WHERE user.id = :userId")
    void updateTweetCount(@Param("increaseCount") boolean increaseCount, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User user SET user.mediaTweetCount = " +
            "CASE WHEN :increaseCount = true THEN (user.mediaTweetCount + 1) " +
            "ELSE (user.mediaTweetCount - 1) " +
            "END " +
            "WHERE user.id = :userId")
    void updateMediaTweetCount(@Param("increaseCount") boolean increaseCount, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User user SET user.profileStarted = true WHERE user.id = :userId")
    void updateProfileStarted(@Param("userId") Long userId);

    @Query("SELECT user FROM User user " +
            "LEFT JOIN user.userBlockedList userBlockedList " +
            "WHERE userBlockedList.id = :authUserId AND user.id IN :userIds")
    List<Long> getUserIdsWhoBlockedMyProfile(@Param("userIds") List<Long> userIds, @Param("authUserId") Long authUserId);

    @Query("SELECT user.id FROM User user " +
            "LEFT JOIN user.following following " +
            "WHERE user.id IN :userIds " +
            "AND (user.privateProfile = false " +
            "   OR (user.privateProfile = true AND (following.id = :authUserId OR user.id = :authUserId)) " +
            "   AND user.active = true)")
    List<Long> getValidUserIdsByIds(@Param("userIds") List<Long> userIds, @Param("authUserId") Long authUserId);

    @Modifying
    @Query("UPDATE User user SET user.pinnedTweetId = :tweetId WHERE user.id = :userId")
    void updatePinnedTweetId(@Param("tweetId") Long tweetId, @Param("userId") Long userId);

}
