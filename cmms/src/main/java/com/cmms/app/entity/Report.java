package com.cmms.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.OffsetDateTime;

@Entity
@Table(name = "reports", indexes = {
        @Index(name = "idx_reports_tenant_id", columnList = "tenant_id"),
        @Index(name = "idx_reports_created_by", columnList = "created_by")
})
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "report_type", nullable = false, length = 50)
    private String reportType;

    @Column(name = "generated_at", nullable = false)
    private OffsetDateTime generatedAt = OffsetDateTime.now();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
    private String data;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;
}