package com.vtlamdev.sourcebase.dao.repository;

import com.vtlamdev.sourcebase.dao.model.sql.UserCredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredentialsEntity, UUID> {

    Optional<UserCredentialsEntity> findByUserId(UUID userId);

    Optional<UserCredentialsEntity> findByActivateToken(String activateToken);

    Optional<UserCredentialsEntity> findByResetToken(String resetToken);

    @Transactional
    void deleteByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("update UserCredentialsEntity c set c.lastLoginTs = :lastLoginTs where c.userId = :userId")
    void updateLastLoginTsByUserId(UUID userId, long lastLoginTs);

    @Transactional
    @Modifying
    @Query("update UserCredentialsEntity c set c.failedLoginAttempts = :failedLoginAttempts where c.userId = :userId")
    void updateFailedLoginAttemptsByUserId(UUID userId, int failedLoginAttempts);

}
