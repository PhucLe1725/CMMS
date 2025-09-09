package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "iot_sensors")
@Data
public class IotSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private Long sensorId;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "sensor_type", nullable = false, length = 50)
    private String sensorType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data_value", columnDefinition = "jsonb")
    private String dataValue;

    @Column(name = "recorded_at")
    private OffsetDateTime recordedAt = OffsetDateTime.now();
}
