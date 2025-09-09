package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "preventive_maintenance", indexes = {
        @Index(name = "idx_preventive_maintenance_asset_id", columnList = "asset_id")
})
@Data
public class PreventiveMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pm_id")
    private Long pmId;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "task_description", nullable = false, columnDefinition = "TEXT")
    private String taskDescription;

    @Column(name = "frequency_type", nullable = false, length = 20)
    private String frequencyType;

    @Column(name = "frequency_interval", nullable = false)
    private Integer frequencyInterval;

    @Column(name = "next_due_date", nullable = false)
    private OffsetDateTime nextDueDate;

    @Column(name = "last_completed")
    private OffsetDateTime lastCompleted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
}