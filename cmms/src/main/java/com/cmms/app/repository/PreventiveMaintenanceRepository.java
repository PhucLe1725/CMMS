package com.cmms.app.repository;

import com.cmms.app.entity.PreventiveMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreventiveMaintenanceRepository extends JpaRepository<PreventiveMaintenance, Long> {
    List<PreventiveMaintenance> findByAssetId(Long assetId);
}
