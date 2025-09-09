package com.cmms.app.repository;

import com.cmms.app.entity.MaintenanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MaintenanceHistoryRepository extends JpaRepository<MaintenanceHistory, Long> {
    List<MaintenanceHistory> findByAssetId(Long assetId);
    List<MaintenanceHistory> findByWorkOrderId(Long workOrderId);
    List<MaintenanceHistory> findByPerformedBy(Long performedBy);
    
    @Query("SELECT m FROM MaintenanceHistory m WHERE m.performedAt BETWEEN :startDate AND :endDate")
    List<MaintenanceHistory> findByPerformedAtBetween(@Param("startDate") OffsetDateTime startDate, 
                                                     @Param("endDate") OffsetDateTime endDate);
    
    @Query("SELECT m FROM MaintenanceHistory m WHERE m.assetId = :assetId AND m.performedAt BETWEEN :startDate AND :endDate")
    List<MaintenanceHistory> findByAssetIdAndPerformedAtBetween(@Param("assetId") Long assetId,
                                                               @Param("startDate") OffsetDateTime startDate,
                                                               @Param("endDate") OffsetDateTime endDate);
}
