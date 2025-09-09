package com.cmms.app.mapper;

import com.cmms.app.dto.iotSensor.request.IotSensorCreateRequest;
import com.cmms.app.dto.iotSensor.request.IotSensorUpdateRequest;
import com.cmms.app.dto.iotSensor.response.IotSensorResponse;
import com.cmms.app.entity.IotSensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IotSensorMapper {
    
    @Mapping(target = "sensorId", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    IotSensor toEntity(IotSensorCreateRequest request);
    
    IotSensorResponse toResponse(IotSensor entity);
    
    List<IotSensorResponse> toResponseList(List<IotSensor> entities);
    
    @Mapping(target = "recordedAt", ignore = true)
    void updateIotSensor(@MappingTarget IotSensor iotSensor, IotSensorUpdateRequest request);
}
