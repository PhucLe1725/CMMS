package com.cmms.app.repository;

import com.cmms.app.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    List<Inventory> findByTenantId(Long tenantId);
    
    Optional<Inventory> findByPartNumber(String partNumber);
    
    List<Inventory> findByLocationId(Long locationId);
    
    List<Inventory> findBySupplierId(Long supplierId);
    
    @Query("SELECT i FROM Inventory i WHERE i.quantity < i.minQuantity")
    List<Inventory> findByQuantityLessThanMinQuantity();
    
    boolean existsByPartNumber(String partNumber);
    
    @Query(value = "SELECT * FROM inventory WHERE tenant_id = :tenantId ORDER BY " +
           "CASE WHEN :sortBy = 'partName' AND :sortDirection = 'asc' THEN part_name END ASC, " +
           "CASE WHEN :sortBy = 'partName' AND :sortDirection = 'desc' THEN part_name END DESC, " +
           "CASE WHEN :sortBy = 'partNumber' AND :sortDirection = 'asc' THEN part_number END ASC, " +
           "CASE WHEN :sortBy = 'partNumber' AND :sortDirection = 'desc' THEN part_number END DESC, " +
           "CASE WHEN :sortBy = 'quantity' AND :sortDirection = 'asc' THEN quantity END ASC, " +
           "CASE WHEN :sortBy = 'quantity' AND :sortDirection = 'desc' THEN quantity END DESC, " +
           "CASE WHEN :sortBy = 'minQuantity' AND :sortDirection = 'asc' THEN min_quantity END ASC, " +
           "CASE WHEN :sortBy = 'minQuantity' AND :sortDirection = 'desc' THEN min_quantity END DESC, " +
           "CASE WHEN :sortBy = 'unitCost' AND :sortDirection = 'asc' THEN unit_cost END ASC, " +
           "CASE WHEN :sortBy = 'unitCost' AND :sortDirection = 'desc' THEN unit_cost END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN part_id END ASC, " +
           "part_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<Inventory> findAllInventoriesByTenantIdWithSortAndLimit(@Param("tenantId") Long tenantId, 
                                                               @Param("skipCount") Integer skipCount, 
                                                               @Param("maxResultCount") Integer maxResultCount,
                                                               @Param("sortBy") String sortBy,
                                                               @Param("sortDirection") String sortDirection);
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.tenantId = :tenantId")
    long countByTenantId(Long tenantId);
    
    @Query(value = "SELECT * FROM inventory WHERE tenant_id = :tenantId AND quantity < min_quantity ORDER BY " +
           "CASE WHEN :sortBy = 'partName' AND :sortDirection = 'asc' THEN part_name END ASC, " +
           "CASE WHEN :sortBy = 'partName' AND :sortDirection = 'desc' THEN part_name END DESC, " +
           "CASE WHEN :sortBy = 'quantity' AND :sortDirection = 'asc' THEN quantity END ASC, " +
           "CASE WHEN :sortBy = 'quantity' AND :sortDirection = 'desc' THEN quantity END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN part_id END ASC, " +
           "part_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<Inventory> findLowStockInventoriesByTenantIdWithSortAndLimit(@Param("tenantId") Long tenantId,
                                                                    @Param("skipCount") Integer skipCount, 
                                                                    @Param("maxResultCount") Integer maxResultCount,
                                                                    @Param("sortBy") String sortBy,
                                                                    @Param("sortDirection") String sortDirection);
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.tenantId = :tenantId AND i.quantity < i.minQuantity")
    long countLowStockByTenantId(Long tenantId);
    
    @Query(value = "SELECT * FROM inventory WHERE tenant_id = :tenantId AND location_id = :locationId ORDER BY " +
           "CASE WHEN :sortBy = 'partName' AND :sortDirection = 'asc' THEN part_name END ASC, " +
           "CASE WHEN :sortBy = 'partName' AND :sortDirection = 'desc' THEN part_name END DESC, " +
           "CASE WHEN :sortBy = 'quantity' AND :sortDirection = 'asc' THEN quantity END ASC, " +
           "CASE WHEN :sortBy = 'quantity' AND :sortDirection = 'desc' THEN quantity END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN part_id END ASC, " +
           "part_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<Inventory> findInventoriesByLocationWithSortAndLimit(@Param("tenantId") Long tenantId,
                                                            @Param("locationId") Long locationId,
                                                            @Param("skipCount") Integer skipCount, 
                                                            @Param("maxResultCount") Integer maxResultCount,
                                                            @Param("sortBy") String sortBy,
                                                            @Param("sortDirection") String sortDirection);
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.tenantId = :tenantId AND i.locationId = :locationId")
    long countByTenantIdAndLocationId(Long tenantId, Long locationId);
}
