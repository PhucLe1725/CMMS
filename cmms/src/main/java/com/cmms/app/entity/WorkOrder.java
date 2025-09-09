package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "work_orders", indexes = {
        @Index(name = "idx_work_orders_tenant_id", columnList = "tenant_id"),
        @Index(name = "idx_work_orders_asset_id", columnList = "asset_id"),
        @Index(name = "idx_work_orders_status", columnList = "status")
})
@Data
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_order_id")
    private Long workOrderId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "priority", nullable = false, length = 20)
    private String priority;

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "scheduled_date")
    private OffsetDateTime scheduledDate;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "estimated_hours", precision = 5, scale = 2)
    private BigDecimal estimatedHours;

    @Column(name = "actual_hours", precision = 5, scale = 2)
    private BigDecimal actualHours;
}