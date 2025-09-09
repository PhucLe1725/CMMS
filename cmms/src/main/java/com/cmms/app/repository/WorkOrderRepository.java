package com.cmms.app.repository;

import com.cmms.app.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByTenantId(Long tenantId);
    List<WorkOrder> findByAssetId(Long assetId);
    List<WorkOrder> findByStatus(String status);
    
    @Query(value = "SELECT * FROM work_orders WHERE tenant_id = :tenantId ORDER BY " +
           "CASE WHEN :sortBy = 'title' AND :sortDirection = 'asc' THEN title END ASC, " +
           "CASE WHEN :sortBy = 'title' AND :sortDirection = 'desc' THEN title END DESC, " +
           "CASE WHEN :sortBy = 'status' AND :sortDirection = 'asc' THEN status END ASC, " +
           "CASE WHEN :sortBy = 'status' AND :sortDirection = 'desc' THEN status END DESC, " +
           "CASE WHEN :sortBy = 'priority' AND :sortDirection = 'asc' THEN priority END ASC, " +
           "CASE WHEN :sortBy = 'priority' AND :sortDirection = 'desc' THEN priority END DESC, " +
           "CASE WHEN :sortBy = 'scheduledDate' AND :sortDirection = 'asc' THEN scheduled_date END ASC, " +
           "CASE WHEN :sortBy = 'scheduledDate' AND :sortDirection = 'desc' THEN scheduled_date END DESC, " +
           "CASE WHEN :sortBy = 'completedAt' AND :sortDirection = 'asc' THEN completed_at END ASC, " +
           "CASE WHEN :sortBy = 'completedAt' AND :sortDirection = 'desc' THEN completed_at END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN work_order_id END ASC, " +
           "work_order_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<WorkOrder> findAllWorkOrdersByTenantIdWithSortAndLimit(@Param("tenantId") Long tenantId, 
                                                               @Param("skipCount") Integer skipCount, 
                                                               @Param("maxResultCount") Integer maxResultCount,
                                                               @Param("sortBy") String sortBy,
                                                               @Param("sortDirection") String sortDirection);
    
    @Query(value = "SELECT * FROM work_orders ORDER BY " +
           "CASE WHEN :sortBy = 'title' AND :sortDirection = 'asc' THEN title END ASC, " +
           "CASE WHEN :sortBy = 'title' AND :sortDirection = 'desc' THEN title END DESC, " +
           "CASE WHEN :sortBy = 'status' AND :sortDirection = 'asc' THEN status END ASC, " +
           "CASE WHEN :sortBy = 'status' AND :sortDirection = 'desc' THEN status END DESC, " +
           "CASE WHEN :sortBy = 'priority' AND :sortDirection = 'asc' THEN priority END ASC, " +
           "CASE WHEN :sortBy = 'priority' AND :sortDirection = 'desc' THEN priority END DESC, " +
           "CASE WHEN :sortBy = 'scheduledDate' AND :sortDirection = 'asc' THEN scheduled_date END ASC, " +
           "CASE WHEN :sortBy = 'scheduledDate' AND :sortDirection = 'desc' THEN scheduled_date END DESC, " +
           "CASE WHEN :sortBy = 'completedAt' AND :sortDirection = 'asc' THEN completed_at END ASC, " +
           "CASE WHEN :sortBy = 'completedAt' AND :sortDirection = 'desc' THEN completed_at END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN work_order_id END ASC, " +
           "work_order_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<WorkOrder> findAllWorkOrdersWithSortAndLimit(@Param("skipCount") Integer skipCount, 
                                                     @Param("maxResultCount") Integer maxResultCount,
                                                     @Param("sortBy") String sortBy,
                                                     @Param("sortDirection") String sortDirection);
    
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.tenantId = :tenantId")
    long countByTenantId(Long tenantId);
    
    @Query("SELECT COUNT(w) FROM WorkOrder w")
    long countAll();
}
