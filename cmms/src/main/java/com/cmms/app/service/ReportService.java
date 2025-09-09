package com.cmms.app.service;

import com.cmms.app.dto.report.request.ReportCreateRequest;
import com.cmms.app.dto.report.request.ReportUpdateRequest;
import com.cmms.app.dto.report.response.ReportResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.Report;
import com.cmms.app.mapper.ReportMapper;
import com.cmms.app.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportMapper reportMapper;

    public ReportResponse createReport(ReportCreateRequest request) {
        Report entity = reportMapper.toEntity(request);
        entity.setGeneratedAt(OffsetDateTime.now());
        Report saved = reportRepository.save(entity);
        return reportMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ReportResponse getReportById(Long id) {
        Report entity = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
        return reportMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<ReportResponse> getAllReports() {
        List<Report> entities = reportRepository.findAll();
        List<ReportResponse> responses = reportMapper.toResponseList(entities);
        return BaseGetAllResponse.<ReportResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<ReportResponse> getAllByTenant(Long tenantId) {
        List<Report> entities = reportRepository.findByTenantId(tenantId);
        List<ReportResponse> responses = reportMapper.toResponseList(entities);
        return BaseGetAllResponse.<ReportResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<ReportResponse> getReportsByType(String reportType) {
        List<Report> entities = reportRepository.findByReportType(reportType);
        List<ReportResponse> responses = reportMapper.toResponseList(entities);
        return BaseGetAllResponse.<ReportResponse>builder()
                .data(responses)
                .build();
    }

    public ReportResponse updateReport(Long id, ReportUpdateRequest request) {
        Report entity = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
        reportMapper.updateReport(entity, request);
        Report updated = reportRepository.save(entity);
        return reportMapper.toResponse(updated);
    }

    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }
}
