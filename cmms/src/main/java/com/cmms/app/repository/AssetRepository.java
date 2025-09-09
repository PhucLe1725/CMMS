package com.cmms.app.repository;

import com.cmms.app.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    @Query(value = "SELECT * FROM assets WHERE tenant_id = :tenantId ORDER BY " +
           "CASE WHEN :sortBy = 'assetName' AND :sortDirection = 'asc' THEN asset_name END ASC, " +
           "CASE WHEN :sortBy = 'assetName' AND :sortDirection = 'desc' THEN asset_name END DESC, " +
           "CASE WHEN :sortBy = 'serialNumber' AND :sortDirection = 'asc' THEN serial_number END ASC, " +
           "CASE WHEN :sortBy = 'serialNumber' AND :sortDirection = 'desc' THEN serial_number END DESC, " +
           "CASE WHEN :sortBy = 'status' AND :sortDirection = 'asc' THEN status END ASC, " +
           "CASE WHEN :sortBy = 'status' AND :sortDirection = 'desc' THEN status END DESC, " +
           "CASE WHEN :sortBy = 'warrantyExpiry' AND :sortDirection = 'asc' THEN warranty_expiry END ASC, " +
           "CASE WHEN :sortBy = 'warrantyExpiry' AND :sortDirection = 'desc' THEN warranty_expiry END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN asset_id END ASC, " +
           "asset_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<Asset> findAllAssetsByTenantIdWithSortAndLimit(@Param("tenantId") Long tenantId, 
                                                       @Param("skipCount") Integer skipCount, 
                                                       @Param("maxResultCount") Integer maxResultCount,
                                                       @Param("sortBy") String sortBy,
                                                       @Param("sortDirection") String sortDirection);
    
    @Query("SELECT COUNT(a) FROM Asset a WHERE a.tenantId = :tenantId")
    long countByTenantId(Long tenantId);
}