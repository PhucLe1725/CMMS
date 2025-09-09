package com.cmms.app.repository;

import com.cmms.app.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByTenantId(Long tenantId);
    List<Report> findByReportType(String reportType);
    List<Report> findByCreatedBy(Long createdBy);
    List<Report> findByTenantIdAndReportType(Long tenantId, String reportType);
}
