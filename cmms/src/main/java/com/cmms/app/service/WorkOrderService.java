package com.cmms.app.service;

import com.cmms.app.dto.workOrder.request.WorkOrderCreateRequest;
import com.cmms.app.dto.workOrder.request.WorkOrderUpdateRequest;
import com.cmms.app.dto.workOrder.response.WorkOrderResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.WorkOrder;
import com.cmms.app.mapper.WorkOrderMapper;
import com.cmms.app.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class WorkOrderService {
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderMapper workOrderMapper;

    public WorkOrderResponse createWorkOrder(WorkOrderCreateRequest request) {
        WorkOrder entity = workOrderMapper.toEntity(request);
        entity.setCreatedAt(OffsetDateTime.now());
        WorkOrder saved = workOrderRepository.save(entity);
        return workOrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public WorkOrderResponse getWorkOrderById(Long id) {
        WorkOrder entity = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkOrder not found with id: " + id));
        return workOrderMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderResponse> getAllWorkOrders() {
        List<WorkOrder> entities = workOrderRepository.findAll();
        List<WorkOrderResponse> responses = workOrderMapper.toResponseList(entities);
        long totalRecords = workOrderRepository.countAll();
        return BaseGetAllResponse.<WorkOrderResponse>builder()
                .data(responses)
                .totalRecords(totalRecords)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderResponse> getAllWorkOrders(Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<WorkOrder> entities = workOrderRepository.findAllWorkOrdersWithSortAndLimit(skipCount, maxResultCount, sortBy, sortDirection);
        List<WorkOrderResponse> responses = workOrderMapper.toResponseList(entities);
        long totalRecords = workOrderRepository.countAll();
        return BaseGetAllResponse.<WorkOrderResponse>builder()
                .data(responses)
                .totalRecords(totalRecords)
                .build();
    }

    public WorkOrderResponse updateWorkOrder(Long id, WorkOrderUpdateRequest request) {
        WorkOrder entity = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkOrder not found with id: " + id));
        workOrderMapper.updateWorkOrder(entity, request);
        WorkOrder updated = workOrderRepository.save(entity);
        return workOrderMapper.toResponse(updated);
    }

    public void deleteWorkOrder(Long id) {
        if (!workOrderRepository.existsById(id)) {
            throw new RuntimeException("WorkOrder not found with id: " + id);
        }
        workOrderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderResponse> getAllByTenant(Long tenantId) {
        List<WorkOrder> entities = workOrderRepository.findByTenantId(tenantId);
        List<WorkOrderResponse> responses = workOrderMapper.toResponseList(entities);
        long totalRecords = workOrderRepository.countByTenantId(tenantId);
        return BaseGetAllResponse.<WorkOrderResponse>builder()
                .data(responses)
                .totalRecords(totalRecords)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderResponse> getAllByTenant(Long tenantId, Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<WorkOrder> entities = workOrderRepository.findAllWorkOrdersByTenantIdWithSortAndLimit(tenantId, skipCount, maxResultCount, sortBy, sortDirection);
        long totalRecords = workOrderRepository.countByTenantId(tenantId);
        List<WorkOrderResponse> responses = workOrderMapper.toResponseList(entities);
        
        return BaseGetAllResponse.<WorkOrderResponse>builder()
                .data(responses)
                .totalRecords(totalRecords)
                .build();
    }
}
