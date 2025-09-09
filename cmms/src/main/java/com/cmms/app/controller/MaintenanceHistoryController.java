package com.cmms.app.controller;

import com.cmms.app.dto.maintenanceHistory.request.MaintenanceHistoryCreateRequest;
import com.cmms.app.dto.maintenanceHistory.request.MaintenanceHistoryUpdateRequest;
import com.cmms.app.dto.maintenanceHistory.response.MaintenanceHistoryResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.MaintenanceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/maintenance-history")
public class MaintenanceHistoryController {
    @Autowired
    private MaintenanceHistoryService maintenanceHistoryService;

    @PostMapping("/create")
    public ApiResponse<MaintenanceHistoryResponse> createMaintenanceHistory(@RequestBody MaintenanceHistoryCreateRequest request) {
        return ApiResponse.<MaintenanceHistoryResponse>builder()
                .result(maintenanceHistoryService.createMaintenanceHistory(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<MaintenanceHistoryResponse> getMaintenanceHistoryById(@PathVariable Long id) {
        return ApiResponse.<MaintenanceHistoryResponse>builder()
                .result(maintenanceHistoryService.getMaintenanceHistoryById(id))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<MaintenanceHistoryResponse>> getAllMaintenanceHistories() {
        return ApiResponse.<BaseGetAllResponse<MaintenanceHistoryResponse>>builder()
                .result(maintenanceHistoryService.getAllMaintenanceHistories())
                .build();
    }

    @GetMapping("/getByAsset")
    public ApiResponse<BaseGetAllResponse<MaintenanceHistoryResponse>> getMaintenanceHistoriesByAssetId(@RequestParam Long assetId) {
        return ApiResponse.<BaseGetAllResponse<MaintenanceHistoryResponse>>builder()
                .result(maintenanceHistoryService.getMaintenanceHistoriesByAssetId(assetId))
                .build();
    }

    @GetMapping("/getByWorkOrder")
    public ApiResponse<BaseGetAllResponse<MaintenanceHistoryResponse>> getMaintenanceHistoriesByWorkOrderId(@RequestParam Long workOrderId) {
        return ApiResponse.<BaseGetAllResponse<MaintenanceHistoryResponse>>builder()
                .result(maintenanceHistoryService.getMaintenanceHistoriesByWorkOrderId(workOrderId))
                .build();
    }

    @GetMapping("/getByPerformedBy")
    public ApiResponse<BaseGetAllResponse<MaintenanceHistoryResponse>> getMaintenanceHistoriesByPerformedBy(@RequestParam Long performedBy) {
        return ApiResponse.<BaseGetAllResponse<MaintenanceHistoryResponse>>builder()
                .result(maintenanceHistoryService.getMaintenanceHistoriesByPerformedBy(performedBy))
                .build();
    }

    @GetMapping("/getByDateRange")
    public ApiResponse<BaseGetAllResponse<MaintenanceHistoryResponse>> getMaintenanceHistoriesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {
        return ApiResponse.<BaseGetAllResponse<MaintenanceHistoryResponse>>builder()
                .result(maintenanceHistoryService.getMaintenanceHistoriesByDateRange(startDate, endDate))
                .build();
    }

    @GetMapping("/getByAssetAndDateRange")
    public ApiResponse<BaseGetAllResponse<MaintenanceHistoryResponse>> getMaintenanceHistoriesByAssetIdAndDateRange(
            @RequestParam Long assetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {
        return ApiResponse.<BaseGetAllResponse<MaintenanceHistoryResponse>>builder()
                .result(maintenanceHistoryService.getMaintenanceHistoriesByAssetIdAndDateRange(assetId, startDate, endDate))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<MaintenanceHistoryResponse> updateMaintenanceHistory(@PathVariable Long id, @RequestBody MaintenanceHistoryUpdateRequest request) {
        return ApiResponse.<MaintenanceHistoryResponse>builder()
                .result(maintenanceHistoryService.updateMaintenanceHistory(id, request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteMaintenanceHistory(@PathVariable Long id) {
        maintenanceHistoryService.deleteMaintenanceHistory(id);
        return ApiResponse.<Void>builder().build();
    }
}
