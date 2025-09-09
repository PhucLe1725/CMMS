package com.cmms.app.service;

import com.cmms.app.dto.workOrderPart.request.WorkOrderPartCreateRequest;
import com.cmms.app.dto.workOrderPart.request.WorkOrderPartUpdateRequest;
import com.cmms.app.dto.workOrderPart.response.WorkOrderPartResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.WorkOrderPart;
import com.cmms.app.entity.WorkOrderPart.WorkOrderPartId;
import com.cmms.app.mapper.WorkOrderPartMapper;
import com.cmms.app.repository.WorkOrderPartRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class WorkOrderPartService {
    @Autowired
    private WorkOrderPartRepository workOrderPartRepository;
    @Autowired
    private WorkOrderPartMapper workOrderPartMapper;

    public WorkOrderPartResponse createWorkOrderPart(WorkOrderPartCreateRequest request) {
        WorkOrderPart entity = workOrderPartMapper.toEntity(request);
        if (entity.getId() == null) {
            entity.setId(new WorkOrderPartId());
        }
        entity.getId().setWorkOrderId(request.getWorkOrderId());
        entity.getId().setPartId(request.getPartId());
        WorkOrderPart saved = workOrderPartRepository.save(entity);
        return workOrderPartMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public WorkOrderPartResponse getWorkOrderPartById(Long workOrderId, Long partId) {
        WorkOrderPartId id = new WorkOrderPartId();
        id.setWorkOrderId(workOrderId);
        id.setPartId(partId);
        WorkOrderPart entity = workOrderPartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkOrderPart not found with workOrderId: " + workOrderId + " and partId: " + partId));
        return workOrderPartMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderPartResponse> getAllWorkOrderParts() {
        List<WorkOrderPart> entities = workOrderPartRepository.findAll();
        List<WorkOrderPartResponse> responses = workOrderPartMapper.toResponseList(entities);
        return BaseGetAllResponse.<WorkOrderPartResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderPartResponse> getWorkOrderPartsByWorkOrderId(Long workOrderId) {
        List<WorkOrderPart> entities = workOrderPartRepository.findByIdWorkOrderId(workOrderId);
        List<WorkOrderPartResponse> responses = workOrderPartMapper.toResponseList(entities);
        return BaseGetAllResponse.<WorkOrderPartResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<WorkOrderPartResponse> getWorkOrderPartsByPartId(Long partId) {
        List<WorkOrderPart> entities = workOrderPartRepository.findByIdPartId(partId);
        List<WorkOrderPartResponse> responses = workOrderPartMapper.toResponseList(entities);
        return BaseGetAllResponse.<WorkOrderPartResponse>builder()
                .data(responses)
                .build();
    }

    public WorkOrderPartResponse updateWorkOrderPart(Long workOrderId, Long partId, WorkOrderPartUpdateRequest request) {
        WorkOrderPartId id = new WorkOrderPartId();
        id.setWorkOrderId(workOrderId);
        id.setPartId(partId);
        WorkOrderPart entity = workOrderPartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkOrderPart not found with workOrderId: " + workOrderId + " and partId: " + partId));
        workOrderPartMapper.updateWorkOrderPart(entity, request);
        WorkOrderPart updated = workOrderPartRepository.save(entity);
        return workOrderPartMapper.toResponse(updated);
    }

    public void deleteWorkOrderPart(Long workOrderId, Long partId) {
        WorkOrderPartId id = new WorkOrderPartId();
        id.setWorkOrderId(workOrderId);
        id.setPartId(partId);
        if (!workOrderPartRepository.existsById(id)) {
            throw new RuntimeException("WorkOrderPart not found with workOrderId: " + workOrderId + " and partId: " + partId);
        }
        workOrderPartRepository.deleteById(id);
    }
}
