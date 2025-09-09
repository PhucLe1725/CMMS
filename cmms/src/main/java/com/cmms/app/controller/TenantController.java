package com.cmms.app.controller;

import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.dto.tenant.response.TenantResponse;
import com.cmms.app.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.cmms.app.dto.tenant.request.TenantCreateRequest;
import com.cmms.app.dto.tenant.request.TenantUpdateRequest;
import com.cmms.app.dto.tenant.response.TenantResponse;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    // Add methods to handle tenant-related requests here
    // For example:
    @GetMapping("/{id}")
    public ApiResponse<TenantResponse> getTenantById(@PathVariable Long id) {
        TenantResponse tenantResponse = tenantService.getTenantById(id);
        return ApiResponse.<TenantResponse>builder()
                .result(tenantResponse)
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<TenantResponse>> getAllTenants() {
        BaseGetAllResponse<TenantResponse> response = tenantService.getAllTenants();
        return ApiResponse.<BaseGetAllResponse<TenantResponse>>builder()
                .result(response)
                .build();
    }

    // Add methods for creating and updating tenants
    @PostMapping("/create")
    public ApiResponse<TenantResponse> createTenant(@RequestBody TenantCreateRequest request) {
        TenantResponse tenantResponse = tenantService.createTenant(request);
        return ApiResponse.<TenantResponse>builder()
                .result(tenantResponse)
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<TenantResponse> updateTenant(@PathVariable Long id, @RequestBody TenantUpdateRequest request) {
        TenantResponse tenantResponse = tenantService.updateTenant(request);
        return ApiResponse.<TenantResponse>builder()
                .result(tenantResponse)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

}
