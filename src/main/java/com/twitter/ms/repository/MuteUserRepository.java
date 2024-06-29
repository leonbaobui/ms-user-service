package com.twitter.ms.repository;

import com.twitter.ms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MuteUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT CASE WHEN count(userMuted) > 0 THEN true ELSE false END FROM User user " +
            "LEFT JOIN user.userMutedList userMuted " +
            "WHERE user.id = :userId " +
            "AND userMuted.id = :mutedUserId")
    boolean isUserMuted(@Param("userId") Long userId, @Param("mutedUserId") Long mutedUserId);

}
