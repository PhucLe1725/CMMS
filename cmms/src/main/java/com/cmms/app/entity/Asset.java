package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "assets")
@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "asset_type_id", nullable = false)
    private Long assetTypeId;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "purchase_date")
    private OffsetDateTime purchaseDate;

    @Column(name = "warranty_expiry")
    private OffsetDateTime warrantyExpiry;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}