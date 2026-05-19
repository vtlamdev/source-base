package com.vtlamdev.sourcebase.dao.repository;

import com.vtlamdev.sourcebase.dao.model.sql.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<TenantEntity> findByEmail(String email);
}
