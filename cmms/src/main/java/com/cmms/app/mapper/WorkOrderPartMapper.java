package com.cmms.app.mapper;

import com.cmms.app.dto.workOrderPart.request.WorkOrderPartCreateRequest;
import com.cmms.app.dto.workOrderPart.request.WorkOrderPartUpdateRequest;
import com.cmms.app.dto.workOrderPart.response.WorkOrderPartResponse;
import com.cmms.app.entity.WorkOrderPart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkOrderPartMapper {
    
    @Mapping(target = "id.workOrderId", source = "workOrderId")
    @Mapping(target = "id.partId", source = "partId")
    WorkOrderPart toEntity(WorkOrderPartCreateRequest request);

    @Mapping(target = "workOrderId", source = "id.workOrderId")
    @Mapping(target = "partId", source = "id.partId")
    WorkOrderPartResponse toResponse(WorkOrderPart entity);

    List<WorkOrderPartResponse> toResponseList(List<WorkOrderPart> entities);

    @Mapping(target = "id.workOrderId", source = "workOrderId")
    @Mapping(target = "id.partId", source = "partId")
    void updateWorkOrderPart(@MappingTarget WorkOrderPart entity, WorkOrderPartUpdateRequest request);
}
