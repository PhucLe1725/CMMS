package com.cmms.app.controller;

import com.cmms.app.dto.report.request.ReportCreateRequest;
import com.cmms.app.dto.report.request.ReportUpdateRequest;
import com.cmms.app.dto.report.response.ReportResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("/create")
    public ApiResponse<ReportResponse> createReport(@RequestBody ReportCreateRequest request) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.createReport(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ReportResponse> getReportById(@PathVariable Long id) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.getReportById(id))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<ReportResponse>> getAllReports() {
        return ApiResponse.<BaseGetAllResponse<ReportResponse>>builder()
                .result(reportService.getAllReports())
                .build();
    }

    @GetMapping("/getAllByTenant")
    public ApiResponse<BaseGetAllResponse<ReportResponse>> getAllByTenant(@RequestParam Long tenantId) {
        return ApiResponse.<BaseGetAllResponse<ReportResponse>>builder()
                .result(reportService.getAllByTenant(tenantId))
                .build();
    }

    @GetMapping("/getByType")
    public ApiResponse<BaseGetAllResponse<ReportResponse>> getReportsByType(@RequestParam String reportType) {
        return ApiResponse.<BaseGetAllResponse<ReportResponse>>builder()
                .result(reportService.getReportsByType(reportType))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ReportResponse> updateReport(@PathVariable Long id, @RequestBody ReportUpdateRequest request) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.updateReport(id, request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ApiResponse.<Void>builder().build();
    }
}
