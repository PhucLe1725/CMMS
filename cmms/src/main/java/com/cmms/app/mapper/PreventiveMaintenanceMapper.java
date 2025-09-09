package com.cmms.app.mapper;

import com.cmms.app.dto.preventiveMaintenance.request.PreventiveMaintenanceCreateRequest;
import com.cmms.app.dto.preventiveMaintenance.request.PreventiveMaintenanceUpdateRequest;
import com.cmms.app.dto.preventiveMaintenance.response.PreventiveMaintenanceResponse;
import com.cmms.app.entity.PreventiveMaintenance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PreventiveMaintenanceMapper {
    @Mapping(target = "pmId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PreventiveMaintenance toEntity(PreventiveMaintenanceCreateRequest request);

    PreventiveMaintenanceResponse toResponse(PreventiveMaintenance entity);

    List<PreventiveMaintenanceResponse> toResponseList(List<PreventiveMaintenance> entities);

    @Mapping(target = "createdAt", ignore = true)
    void updatePreventiveMaintenance(@MappingTarget PreventiveMaintenance entity, PreventiveMaintenanceUpdateRequest request);
}
