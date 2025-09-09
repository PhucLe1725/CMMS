package com.cmms.app.repository;

import com.cmms.app.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTenantId(Long tenantId);
    List<Notification> findByUserId(Long userId);
    List<Notification> findByWorkOrderId(Long workOrderId);
    List<Notification> findByAssetId(Long assetId);
    List<Notification> findByType(String type);
    List<Notification> findByIsRead(Boolean isRead);
    
    List<Notification> findByTenantIdAndUserId(Long tenantId, Long userId);
    List<Notification> findByTenantIdAndUserIdAndIsRead(Long tenantId, Long userId, Boolean isRead);
    
    @Query("SELECT n FROM Notification n WHERE n.tenantId = :tenantId AND n.userId = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByTenantIdAndUserIdOrderByCreatedAtDesc(@Param("tenantId") Long tenantId, @Param("userId") Long userId);
}
