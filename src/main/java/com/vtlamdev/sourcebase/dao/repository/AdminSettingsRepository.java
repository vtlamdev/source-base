package com.vtlamdev.sourcebase.dao.repository;

import com.vtlamdev.sourcebase.dao.model.sql.AdminSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AdminSettingsRepository extends JpaRepository<AdminSettingsEntity, UUID>, JpaSpecificationExecutor<AdminSettingsEntity> {

    @Query(value = "select a from #{#entityName} a where a.id = :id",
            queryRewriter = SoftDeleteQueryRewriter.class)
    AdminSettingsEntity findActiveById(@Param("id") UUID id);

    @Query(value = "select a from #{#entityName} a where a.tenantId = :tenantId and a.key = :key",
            queryRewriter = SoftDeleteQueryRewriter.class)
    AdminSettingsEntity findByTenantIdAndKey(@Param("tenantId") UUID tenantId, @Param("key") String key);

    @Query(value = "select count(a) from #{#entityName} a where a.tenantId = :tenantId",
            queryRewriter = SoftDeleteQueryRewriter.class)
    long countByTenantId(@Param("tenantId") UUID tenantId);

    @Modifying
    @Query("update #{#entityName} a set a.deleted = true, a.deletedTime = :deletedTime where a.tenantId = :tenantId and a.key = :key and a.deleted = false")
    int softDeleteByTenantIdAndKey(@Param("tenantId") UUID tenantId,
                                   @Param("key") String key,
                                   @Param("deletedTime") long deletedTime);

    @Modifying
    @Query("update #{#entityName} a set a.deleted = true, a.deletedTime = :deletedTime where a.tenantId = :tenantId and a.deleted = false")
    int softDeleteByTenantId(@Param("tenantId") UUID tenantId, @Param("deletedTime") long deletedTime);

}
