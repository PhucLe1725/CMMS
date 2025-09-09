package com.cmms.app.service;

import com.cmms.app.dto.preventiveMaintenance.request.PreventiveMaintenanceCreateRequest;
import com.cmms.app.dto.preventiveMaintenance.request.PreventiveMaintenanceUpdateRequest;
import com.cmms.app.dto.preventiveMaintenance.response.PreventiveMaintenanceResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.PreventiveMaintenance;
import com.cmms.app.mapper.PreventiveMaintenanceMapper;
import com.cmms.app.repository.PreventiveMaintenanceRepository;
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
public class PreventiveMaintenanceService {
    @Autowired
    private PreventiveMaintenanceRepository preventiveMaintenanceRepository;
    @Autowired
    private PreventiveMaintenanceMapper preventiveMaintenanceMapper;

    public PreventiveMaintenanceResponse createPreventiveMaintenance(PreventiveMaintenanceCreateRequest request) {
        PreventiveMaintenance entity = preventiveMaintenanceMapper.toEntity(request);
        entity.setCreatedAt(OffsetDateTime.now());
        PreventiveMaintenance saved = preventiveMaintenanceRepository.save(entity);
        return preventiveMaintenanceMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PreventiveMaintenanceResponse getPreventiveMaintenanceById(Long id) {
        PreventiveMaintenance entity = preventiveMaintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreventiveMaintenance not found with id: " + id));
        return preventiveMaintenanceMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<PreventiveMaintenanceResponse> getAllPreventiveMaintenances() {
        List<PreventiveMaintenance> entities = preventiveMaintenanceRepository.findAll();
        List<PreventiveMaintenanceResponse> responses = preventiveMaintenanceMapper.toResponseList(entities);
        return BaseGetAllResponse.<PreventiveMaintenanceResponse>builder().data(responses).build();
    }

    public PreventiveMaintenanceResponse updatePreventiveMaintenance(Long id, PreventiveMaintenanceUpdateRequest request) {
        PreventiveMaintenance entity = preventiveMaintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreventiveMaintenance not found with id: " + id));
        preventiveMaintenanceMapper.updatePreventiveMaintenance(entity, request);
        PreventiveMaintenance updated = preventiveMaintenanceRepository.save(entity);
        return preventiveMaintenanceMapper.toResponse(updated);
    }

    public void deletePreventiveMaintenance(Long id) {
        if (!preventiveMaintenanceRepository.existsById(id)) {
            throw new RuntimeException("PreventiveMaintenance not found with id: " + id);
        }
        preventiveMaintenanceRepository.deleteById(id);
    }
}
