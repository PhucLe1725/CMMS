package com.cmms.app.mapper;

import com.cmms.app.dto.workOrder.request.WorkOrderCreateRequest;
import com.cmms.app.dto.workOrder.request.WorkOrderUpdateRequest;
import com.cmms.app.dto.workOrder.response.WorkOrderResponse;
import com.cmms.app.entity.WorkOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkOrderMapper {
    @Mapping(target = "workOrderId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    WorkOrder toEntity(WorkOrderCreateRequest request);

    WorkOrderResponse toResponse(WorkOrder entity);

    List<WorkOrderResponse> toResponseList(List<WorkOrder> entities);

    @Mapping(target = "createdAt", ignore = true)
    void updateWorkOrder(@MappingTarget WorkOrder entity, WorkOrderUpdateRequest request);
}
