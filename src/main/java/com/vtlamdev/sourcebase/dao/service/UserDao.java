package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.PageLink;
import com.vtlamdev.sourcebase.dao.Dao;
import com.vtlamdev.sourcebase.dao.TenantEntityDao;

import java.util.Optional;

public interface UserDao extends Dao<User>, TenantEntityDao<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByTenantIdAndEmail(TenantId tenantId, String email);

    @Override
    PageData<User> findAllByTenantId(TenantId tenantId, PageLink pageLink);

    @Override
    Long countByTenantId(TenantId tenantId);

}
