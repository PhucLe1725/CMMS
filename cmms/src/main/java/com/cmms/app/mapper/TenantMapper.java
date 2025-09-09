package com.cmms.app.mapper;

import com.cmms.app.dto.tenant.request.TenantCreateRequest;
import com.cmms.app.dto.tenant.request.TenantUpdateRequest;
import com.cmms.app.dto.tenant.response.TenantResponse;
import com.cmms.app.entity.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    Tenant toTenant(TenantCreateRequest request);

    TenantResponse toTenantResponse(Tenant tenant);

    void updateTenant(@MappingTarget Tenant tenant, TenantUpdateRequest request);
}
