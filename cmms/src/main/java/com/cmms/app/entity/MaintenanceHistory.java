package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "maintenance_history", indexes = {
        @Index(name = "idx_maintenance_history_asset_id", columnList = "asset_id")
})
@Data
public class MaintenanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "work_order_id")
    private Long workOrderId;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "action", nullable = false, columnDefinition = "TEXT")
    private String action;

    @Column(name = "performed_by", nullable = false)
    private Long performedBy;

    @Column(name = "performed_at", nullable = false)
    private OffsetDateTime performedAt = OffsetDateTime.now();
}