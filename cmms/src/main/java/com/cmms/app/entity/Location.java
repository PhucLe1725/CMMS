package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = true)
    private String description;

    @Column(name = "parent_location_id", nullable = true)
    private Long parentLocationId;
}
