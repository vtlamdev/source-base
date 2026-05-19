package com.vtlamdev.sourcebase.dao;

import com.vtlamdev.sourcebase.common.data.EntityType;
import com.vtlamdev.sourcebase.common.data.HasVersion;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.dao.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class JpaAbstractDao<E extends BaseEntity<D>, D> implements Dao<D> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public D save(TenantId tenantId, D domain) {
        return saveInternal(domain, false);
    }

    @Override
    @Transactional
    public D saveAndFlush(TenantId tenantId, D domain) {
        return saveInternal(domain, true);
    }

    private D saveInternal(D domain, boolean flush) {
        E entity = toEntity(domain);
        boolean isNew = entity.getUuid() == null;
        if (isNew) {
            entity.setUuid(UUID.randomUUID());
            entity.setCreatedTime(System.currentTimeMillis());
        } else if (entity.getCreatedTime() <= 0) {
            hydrateExistingVersionedEntity(entity);
        }
        E saved;
        if (isNew) {
            saved = create(entity, flush);
        } else {
            saved = update(entity, flush);
        }
        return saved.toData();
    }

    @SuppressWarnings("unchecked")
    private void hydrateExistingVersionedEntity(E entity) {
        if (!(entity instanceof HasVersion versionedEntity)) {
            return;
        }
        E existingEntity = entityManager.find(getEntityClass(), entity.getUuid());
        if (existingEntity == null) {
            return;
        }
        if (versionedEntity.getVersion() == null && existingEntity instanceof HasVersion existingVersionedEntity) {
            versionedEntity.setVersion(existingVersionedEntity.getVersion());
        }
        if (entity.getCreatedTime() <= 0) {
            entity.setCreatedTime(existingEntity.getCreatedTime());
        }
    }

    private E create(E entity, boolean flush) {
        if (entity instanceof HasVersion) {
            entityManager.persist(entity);
            if (flush) {
                entityManager.flush();
            }
            return entity;
        }
        return flush ? getRepository().saveAndFlush(entity) : getRepository().save(entity);
    }

    @SuppressWarnings("unchecked")
    private E update(E entity, boolean flush) {
        if (entity instanceof HasVersion versionedEntity && versionedEntity.getVersion() == null) {
            E existingEntity = entityManager.find(getEntityClass(), entity.getUuid());
            if (existingEntity != null && existingEntity instanceof HasVersion existingVersionedEntity) {
                versionedEntity.setVersion(existingVersionedEntity.getVersion());
                if (entity.getCreatedTime() <= 0) {
                    entity.setCreatedTime(existingEntity.getCreatedTime());
                }
            } else {
                return create(entity, flush);
            }
        }
        E merged = entityManager.merge(entity);
        if (flush) {
            entityManager.flush();
        }
        return merged;
    }

    @Override
    public List<D> find(TenantId tenantId) {
        return getRepository().findAll().stream().map(BaseEntity::toData).toList();
    }

    @Override
    public D findById(TenantId tenantId, UUID id) {
        return getRepository().findById(id).map(BaseEntity::toData).orElse(null);
    }

    @Override
    public CompletableFuture<D> findByIdAsync(TenantId tenantId, UUID id) {
        return CompletableFuture.completedFuture(findById(tenantId, id));
    }

    @Override
    public boolean existsById(TenantId tenantId, UUID id) {
        return getRepository().existsById(id);
    }

    @Override
    public CompletableFuture<Boolean> existsByIdAsync(TenantId tenantId, UUID id) {
        return CompletableFuture.completedFuture(existsById(tenantId, id));
    }

    @Override
    @Transactional
    public void removeById(TenantId tenantId, UUID id) {
        getRepository().deleteById(id);
    }

    @Override
    @Transactional
    public void removeAllByIds(Collection<UUID> ids) {
        ids.forEach(getRepository()::deleteById);
    }

    @Override
    public List<UUID> findIdsByTenantIdAndIdOffset(TenantId tenantId, UUID idOffset, int limit) {
        return getRepository().findAll().stream()
                .map(BaseEntity::getUuid)
                .filter(id -> idOffset == null || id.compareTo(idOffset) > 0)
                .limit(limit)
                .toList();
    }

    protected abstract JpaRepository<E, UUID> getRepository();

    protected abstract Class<E> getEntityClass();

    public abstract EntityType getEntityType();

    protected E toEntity(D domain) {
        try {
            return getEntityClass().getConstructor(domain.getClass()).newInstance(domain);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Unable to map domain to entity: " + domain.getClass().getName(), e);
        }
    }

}
