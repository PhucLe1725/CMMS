package com.cmms.app.mapper;

import com.cmms.app.dto.maintenanceHistory.request.MaintenanceHistoryCreateRequest;
import com.cmms.app.dto.maintenanceHistory.request.MaintenanceHistoryUpdateRequest;
import com.cmms.app.dto.maintenanceHistory.response.MaintenanceHistoryResponse;
import com.cmms.app.entity.MaintenanceHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceHistoryMapper {
    
    @Mapping(target = "historyId", ignore = true)
    @Mapping(target = "performedAt", ignore = true)
    MaintenanceHistory toEntity(MaintenanceHistoryCreateRequest request);

    MaintenanceHistoryResponse toResponse(MaintenanceHistory entity);

    List<MaintenanceHistoryResponse> toResponseList(List<MaintenanceHistory> entities);

    @Mapping(target = "performedAt", ignore = true)
    void updateMaintenanceHistory(@MappingTarget MaintenanceHistory entity, MaintenanceHistoryUpdateRequest request);
}
