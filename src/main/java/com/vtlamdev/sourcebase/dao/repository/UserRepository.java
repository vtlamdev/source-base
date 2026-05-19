package com.vtlamdev.sourcebase.dao.repository;

import com.vtlamdev.sourcebase.dao.model.sql.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByTenantIdAndEmail(UUID tenantId, String email);

    Page<UserEntity> findByTenantId(UUID tenantId, Pageable pageable);

    long countByTenantId(UUID tenantId);

    Optional<UserEntity> findByEmail(String email);

}
