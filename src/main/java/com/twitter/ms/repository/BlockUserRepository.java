package com.twitter.ms.repository;

import com.twitter.ms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT CASE WHEN count(blockedUser) > 0 THEN true ELSE false END " +
            "FROM User user " +
            "LEFT JOIN user.userBlockedList blockedUser " +
            "WHERE user.id = :userId " +
            "AND blockedUser.id = :blockedUserId")
    boolean isUserBlocked(@Param("userId") Long userId, @Param("blockedUserId") Long blockedUserId);

}
