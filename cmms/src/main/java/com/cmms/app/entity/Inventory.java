package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "inventory", indexes = {
        @Index(name = "idx_inventory_tenant_id", columnList = "tenant_id"),
        @Index(name = "idx_inventory_part_number", columnList = "part_number")
})
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Long partId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "part_name", nullable = false, length = 100)
    private String partName;

    @Column(name = "part_number", nullable = false, length = 50, unique = true)
    private String partNumber;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "supplier_id")
    private Long supplierId;
}