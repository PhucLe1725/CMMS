package com.cmms.app.service;

import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.dto.tenant.request.TenantCreateRequest;
import com.cmms.app.dto.tenant.request.TenantUpdateRequest;
import com.cmms.app.dto.tenant.response.TenantResponse;
import com.cmms.app.entity.Tenant;
import com.cmms.app.mapper.TenantMapper;
import com.cmms.app.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantMapper tenantMapper;

    public TenantResponse getTenantById(Long tenantId) {
        return tenantRepository.findById(tenantId)
                .map(tenantMapper::toTenantResponse)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
    }


    public BaseGetAllResponse<TenantResponse> getAllTenants() {
        List<TenantResponse> tenantResponses = tenantRepository.findAllTenants()
                .stream()
                .map(tenantMapper::toTenantResponse)
                .toList();

        return BaseGetAllResponse.<TenantResponse>builder()
                .data(tenantResponses)
                .build();
    }


    public TenantResponse createTenant(TenantCreateRequest request) {
        Tenant tenant = tenantMapper.toTenant(request);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toTenantResponse(tenant);
    }

    public TenantResponse updateTenant(TenantUpdateRequest request) {
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenantMapper.updateTenant(tenant, request);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toTenantResponse(tenant);
    }

    // Method to delete a tenant
    public void deleteTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenantRepository.delete(tenant);
    }

}
