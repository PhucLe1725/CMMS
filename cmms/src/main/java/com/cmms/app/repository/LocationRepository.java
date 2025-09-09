package com.cmms.app.repository;

import com.cmms.app.dto.location.response.LocationResponse;
import com.cmms.app.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.tenantId = :tenantId")
    List<Location> findAllLocationByTenantId(Long tenantId);
}
