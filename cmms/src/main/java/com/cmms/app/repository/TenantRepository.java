package com.cmms.app.repository;

import com.cmms.app.entity.Asset;
import com.cmms.app.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    @Query("SELECT t FROM Tenant t")
    List<Tenant> findAllTenants();
}

