package com.cmms.app.service;

import com.cmms.app.dto.iotSensor.request.IotSensorCreateRequest;
import com.cmms.app.dto.iotSensor.request.IotSensorUpdateRequest;
import com.cmms.app.dto.iotSensor.response.IotSensorResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.IotSensor;
import com.cmms.app.mapper.IotSensorMapper;
import com.cmms.app.repository.IotSensorRepository;
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
public class IotSensorService {
    
    @Autowired
    private IotSensorRepository iotSensorRepository;
    
    @Autowired
    private IotSensorMapper iotSensorMapper;
    
    public IotSensorResponse createIotSensor(IotSensorCreateRequest request) {
        IotSensor entity = iotSensorMapper.toEntity(request);
        entity.setRecordedAt(OffsetDateTime.now());
        IotSensor savedEntity = iotSensorRepository.save(entity);
        return iotSensorMapper.toResponse(savedEntity);
    }
    
    @Transactional(readOnly = true)
    public IotSensorResponse getIotSensorById(Long id) {
        IotSensor entity = iotSensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IoT Sensor not found with id: " + id));
        return iotSensorMapper.toResponse(entity);
    }
    
    @Transactional(readOnly = true)
    public BaseGetAllResponse<IotSensorResponse> getAllIotSensors() {
        List<IotSensor> entities = iotSensorRepository.findAll();
        List<IotSensorResponse> responses = iotSensorMapper.toResponseList(entities);
        return BaseGetAllResponse.<IotSensorResponse>builder()
                .data(responses)
                .build();
    }
    
    @Transactional(readOnly = true)
    public List<IotSensorResponse> getIotSensorsByAssetId(Long assetId) {
        List<IotSensor> entities = iotSensorRepository.findByAssetId(assetId);
        return iotSensorMapper.toResponseList(entities);
    }
    
    @Transactional(readOnly = true)
    public List<IotSensorResponse> getIotSensorsBySensorType(String sensorType) {
        List<IotSensor> entities = iotSensorRepository.findBySensorType(sensorType);
        return iotSensorMapper.toResponseList(entities);
    }
    
    @Transactional(readOnly = true)
    public List<IotSensorResponse> getIotSensorsByAssetIdAndSensorType(Long assetId, String sensorType) {
        List<IotSensor> entities = iotSensorRepository.findByAssetIdAndSensorType(assetId, sensorType);
        return iotSensorMapper.toResponseList(entities);
    }
    
    @Transactional(readOnly = true)
    public List<IotSensorResponse> getIotSensorsByDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        List<IotSensor> entities = iotSensorRepository.findByRecordedAtBetween(startDate, endDate);
        return iotSensorMapper.toResponseList(entities);
    }
    
    @Transactional(readOnly = true)
    public List<IotSensorResponse> getIotSensorsByAssetIdAndDateRange(Long assetId, OffsetDateTime startDate, OffsetDateTime endDate) {
        List<IotSensor> entities = iotSensorRepository.findByAssetIdAndRecordedAtBetween(assetId, startDate, endDate);
        return iotSensorMapper.toResponseList(entities);
    }
    
    public IotSensorResponse updateIotSensor(Long id, IotSensorUpdateRequest request) {
        IotSensor entity = iotSensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IoT Sensor not found with id: " + id));
        
        iotSensorMapper.updateIotSensor(entity, request);
        IotSensor updatedEntity = iotSensorRepository.save(entity);
        return iotSensorMapper.toResponse(updatedEntity);
    }
    
    public void deleteIotSensor(Long id) {
        if (!iotSensorRepository.existsById(id)) {
            throw new RuntimeException("IoT Sensor not found with id: " + id);
        }
        iotSensorRepository.deleteById(id);
    }
}
