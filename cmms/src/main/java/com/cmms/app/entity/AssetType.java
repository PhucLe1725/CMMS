package com.cmms.app.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "asset_types")
@Data
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_type_id")
    private Long assetTypeId;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(nullable = true)
    private String description;
}
