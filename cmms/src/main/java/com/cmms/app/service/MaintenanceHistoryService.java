package com.cmms.app.service;

import com.cmms.app.dto.maintenanceHistory.request.MaintenanceHistoryCreateRequest;
import com.cmms.app.dto.maintenanceHistory.request.MaintenanceHistoryUpdateRequest;
import com.cmms.app.dto.maintenanceHistory.response.MaintenanceHistoryResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.MaintenanceHistory;
import com.cmms.app.mapper.MaintenanceHistoryMapper;
import com.cmms.app.repository.MaintenanceHistoryRepository;
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
public class MaintenanceHistoryService {
    @Autowired
    private MaintenanceHistoryRepository maintenanceHistoryRepository;
    @Autowired
    private MaintenanceHistoryMapper maintenanceHistoryMapper;

    public MaintenanceHistoryResponse createMaintenanceHistory(MaintenanceHistoryCreateRequest request) {
        MaintenanceHistory entity = maintenanceHistoryMapper.toEntity(request);
        entity.setPerformedAt(OffsetDateTime.now());
        MaintenanceHistory saved = maintenanceHistoryRepository.save(entity);
        return maintenanceHistoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MaintenanceHistoryResponse getMaintenanceHistoryById(Long id) {
        MaintenanceHistory entity = maintenanceHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaintenanceHistory not found with id: " + id));
        return maintenanceHistoryMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<MaintenanceHistoryResponse> getAllMaintenanceHistories() {
        List<MaintenanceHistory> entities = maintenanceHistoryRepository.findAll();
        List<MaintenanceHistoryResponse> responses = maintenanceHistoryMapper.toResponseList(entities);
        return BaseGetAllResponse.<MaintenanceHistoryResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<MaintenanceHistoryResponse> getMaintenanceHistoriesByAssetId(Long assetId) {
        List<MaintenanceHistory> entities = maintenanceHistoryRepository.findByAssetId(assetId);
        List<MaintenanceHistoryResponse> responses = maintenanceHistoryMapper.toResponseList(entities);
        return BaseGetAllResponse.<MaintenanceHistoryResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<MaintenanceHistoryResponse> getMaintenanceHistoriesByWorkOrderId(Long workOrderId) {
        List<MaintenanceHistory> entities = maintenanceHistoryRepository.findByWorkOrderId(workOrderId);
        List<MaintenanceHistoryResponse> responses = maintenanceHistoryMapper.toResponseList(entities);
        return BaseGetAllResponse.<MaintenanceHistoryResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<MaintenanceHistoryResponse> getMaintenanceHistoriesByPerformedBy(Long performedBy) {
        List<MaintenanceHistory> entities = maintenanceHistoryRepository.findByPerformedBy(performedBy);
        List<MaintenanceHistoryResponse> responses = maintenanceHistoryMapper.toResponseList(entities);
        return BaseGetAllResponse.<MaintenanceHistoryResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<MaintenanceHistoryResponse> getMaintenanceHistoriesByDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        List<MaintenanceHistory> entities = maintenanceHistoryRepository.findByPerformedAtBetween(startDate, endDate);
        List<MaintenanceHistoryResponse> responses = maintenanceHistoryMapper.toResponseList(entities);
        return BaseGetAllResponse.<MaintenanceHistoryResponse>builder()
                .data(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public BaseGetAllResponse<MaintenanceHistoryResponse> getMaintenanceHistoriesByAssetIdAndDateRange(Long assetId, OffsetDateTime startDate, OffsetDateTime endDate) {
        List<MaintenanceHistory> entities = maintenanceHistoryRepository.findByAssetIdAndPerformedAtBetween(assetId, startDate, endDate);
        List<MaintenanceHistoryResponse> responses = maintenanceHistoryMapper.toResponseList(entities);
        return BaseGetAllResponse.<MaintenanceHistoryResponse>builder()
                .data(responses)
                .build();
    }

    public MaintenanceHistoryResponse updateMaintenanceHistory(Long id, MaintenanceHistoryUpdateRequest request) {
        MaintenanceHistory entity = maintenanceHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaintenanceHistory not found with id: " + id));
        maintenanceHistoryMapper.updateMaintenanceHistory(entity, request);
        MaintenanceHistory updated = maintenanceHistoryRepository.save(entity);
        return maintenanceHistoryMapper.toResponse(updated);
    }

    public void deleteMaintenanceHistory(Long id) {
        if (!maintenanceHistoryRepository.existsById(id)) {
            throw new RuntimeException("MaintenanceHistory not found with id: " + id);
        }
        maintenanceHistoryRepository.deleteById(id);
    }
}
