package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.BaseData;
import com.vtlamdev.sourcebase.common.data.EntityType;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.PageLink;
import com.vtlamdev.sourcebase.common.data.page.ScrollData;
import com.vtlamdev.sourcebase.dao.DaoUtil;
import com.vtlamdev.sourcebase.dao.JpaAbstractDao;
import com.vtlamdev.sourcebase.dao.model.sql.AdminSettingsEntity;
import com.vtlamdev.sourcebase.dao.repository.AdminSettingsRepository;
import com.vtlamdev.sourcebase.dao.service.AdminSettingsDao;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JpaAdminSettingsDao extends JpaAbstractDao<AdminSettingsEntity, AdminSettings> implements AdminSettingsDao {

    private final AdminSettingsRepository repository;

    public JpaAdminSettingsDao(AdminSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<AdminSettingsEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    protected Class<AdminSettingsEntity> getEntityClass() {
        return AdminSettingsEntity.class;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ADMIN_SETTINGS;
    }

    @Override
    public AdminSettings findById(TenantId tenantId, UUID id) {
        return DaoUtil.getData(repository.findActiveById(id));
    }

    @Override
    public AdminSettings findByTenantIdAndKey(UUID tenantId, String key) {
        return DaoUtil.getData(repository.findByTenantIdAndKey(tenantId, key));
    }

    @Override
    public PageData<AdminSettings> findAllByTenantId(TenantId tenantId, PageLink pageLink) {
        PageRequest pageRequest = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), defaultSort());
        Page<AdminSettingsEntity> page = repository.findBy(buildSpecification(tenantId, pageLink.getTextSearch()),
                query -> query.sortBy(defaultSort()).page(pageRequest));
        return DaoUtil.toPageData(page);
    }

    @Override
    public ScrollData<AdminSettings> findScrollByTenantId(TenantId tenantId, ListQuery listQuery) {
        Window<AdminSettingsEntity> window = repository.findBy(buildSpecification(tenantId, listQuery.getTextSearch()),
                query -> query.sortBy(defaultSort()).limit(listQuery.getLimit()).scroll(decodeCursor(listQuery.getCursor())));
        List<AdminSettings> data = window.stream().map(AdminSettingsEntity::toData).toList();
        String nextCursor = window.hasNext() && !window.isEmpty() ? encodeCursor(window.positionAt(window.size() - 1)) : null;
        return new ScrollData<>(data, listQuery.getLimit(), nextCursor, window.hasNext());
    }

    @Override
    public Long countByTenantId(TenantId tenantId) {
        return repository.countByTenantId(tenantId.getId());
    }

    @Override
    @Transactional
    public boolean removeByTenantIdAndKey(UUID tenantId, String key) {
        return repository.softDeleteByTenantIdAndKey(tenantId, key, System.currentTimeMillis()) > 0;
    }

    @Override
    @Transactional
    public void removeByTenantId(UUID tenantId) {
        repository.softDeleteByTenantId(tenantId, System.currentTimeMillis());
    }

    private Specification<AdminSettingsEntity> buildSpecification(TenantId tenantId, String textSearch) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("tenantId"), tenantId.getId()),
                    criteriaBuilder.isFalse(root.get("deleted"))
            );
            if (StringUtils.hasText(textSearch)) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("key")), "%" + textSearch.toLowerCase() + "%"));
            }
            return predicate;
        };
    }

    private Sort defaultSort() {
        return Sort.by(new Sort.Order(Sort.Direction.DESC, "createdTime"), new Sort.Order(Sort.Direction.DESC, "id"));
    }

    private String encodeCursor(ScrollPosition position) {
        if (!(position instanceof KeysetScrollPosition keysetScrollPosition)) {
            throw new IllegalArgumentException("Unsupported scroll position type: " + position.getClass().getName());
        }
        try {
            String rawValue = BaseData.MAPPER.writeValueAsString(keysetScrollPosition.getKeys());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(rawValue.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to encode scroll cursor", e);
        }
    }

    @SuppressWarnings("unchecked")
    private ScrollPosition decodeCursor(String cursor) {
        if (!StringUtils.hasText(cursor)) {
            return ScrollPosition.keyset();
        }
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
            Map<String, Object> rawKeys = BaseData.MAPPER.readValue(decoded, Map.class);
            Object rawCreatedTime = rawKeys.get("createdTime");
            Object rawId = rawKeys.get("id");
            if (rawCreatedTime == null || rawId == null) {
                throw new IllegalArgumentException("Cursor is missing required keyset values");
            }
            Map<String, Object> keys = Map.of(
                    "createdTime", ((Number) rawCreatedTime).longValue(),
                    "id", UUID.fromString(rawId.toString())
            );
            return ScrollPosition.forward(keys);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid scroll cursor", e);
        }
    }

}
