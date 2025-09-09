package com.cmms.app.controller;

import com.cmms.app.dto.preventiveMaintenance.request.PreventiveMaintenanceCreateRequest;
import com.cmms.app.dto.preventiveMaintenance.request.PreventiveMaintenanceUpdateRequest;
import com.cmms.app.dto.preventiveMaintenance.response.PreventiveMaintenanceResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.PreventiveMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preventive-maintenance")
public class PreventiveMaintenanceController {
    @Autowired
    private PreventiveMaintenanceService preventiveMaintenanceService;

    @PostMapping("/create")
    public ApiResponse<PreventiveMaintenanceResponse> createPreventiveMaintenance(@RequestBody PreventiveMaintenanceCreateRequest request) {
        return ApiResponse.<PreventiveMaintenanceResponse>builder()
                .result(preventiveMaintenanceService.createPreventiveMaintenance(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PreventiveMaintenanceResponse> getPreventiveMaintenanceById(@PathVariable Long id) {
        return ApiResponse.<PreventiveMaintenanceResponse>builder()
                .result(preventiveMaintenanceService.getPreventiveMaintenanceById(id))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<PreventiveMaintenanceResponse>> getAllPreventiveMaintenances() {
        return ApiResponse.<BaseGetAllResponse<PreventiveMaintenanceResponse>>builder()
                .result(preventiveMaintenanceService.getAllPreventiveMaintenances())
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<PreventiveMaintenanceResponse> updatePreventiveMaintenance(@PathVariable Long id, @RequestBody PreventiveMaintenanceUpdateRequest request) {
        return ApiResponse.<PreventiveMaintenanceResponse>builder()
                .result(preventiveMaintenanceService.updatePreventiveMaintenance(id, request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deletePreventiveMaintenance(@PathVariable Long id) {
        preventiveMaintenanceService.deletePreventiveMaintenance(id);
        return ApiResponse.<Void>builder().build();
    }
}
