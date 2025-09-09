package com.cmms.app.controller;

import com.cmms.app.dto.iotSensor.request.IotSensorCreateRequest;
import com.cmms.app.dto.iotSensor.request.IotSensorUpdateRequest;
import com.cmms.app.dto.iotSensor.response.IotSensorResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.IotSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/iot-sensors")
public class IotSensorController {
    
    @Autowired
    private IotSensorService iotSensorService;
    
    @PostMapping("/create")
    public ApiResponse<IotSensorResponse> createIotSensor(@RequestBody IotSensorCreateRequest request) {
        return ApiResponse.<IotSensorResponse>builder()
                .result(iotSensorService.createIotSensor(request))
                .build();
    }
    
    @GetMapping("/{id}")
    public ApiResponse<IotSensorResponse> getIotSensorById(@PathVariable Long id) {
        return ApiResponse.<IotSensorResponse>builder()
                .result(iotSensorService.getIotSensorById(id))
                .build();
    }
    
    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<IotSensorResponse>> getAllIotSensors() {
        return ApiResponse.<BaseGetAllResponse<IotSensorResponse>>builder()
                .result(iotSensorService.getAllIotSensors())
                .build();
    }
    
    @GetMapping("/asset/{assetId}")
    public ApiResponse<List<IotSensorResponse>> getIotSensorsByAssetId(@PathVariable Long assetId) {
        return ApiResponse.<List<IotSensorResponse>>builder()
                .result(iotSensorService.getIotSensorsByAssetId(assetId))
                .build();
    }
    
    @GetMapping("/sensor-type/{sensorType}")
    public ApiResponse<List<IotSensorResponse>> getIotSensorsBySensorType(@PathVariable String sensorType) {
        return ApiResponse.<List<IotSensorResponse>>builder()
                .result(iotSensorService.getIotSensorsBySensorType(sensorType))
                .build();
    }
    
    @GetMapping("/asset/{assetId}/sensor-type/{sensorType}")
    public ApiResponse<List<IotSensorResponse>> getIotSensorsByAssetIdAndSensorType(
            @PathVariable Long assetId, 
            @PathVariable String sensorType) {
        return ApiResponse.<List<IotSensorResponse>>builder()
                .result(iotSensorService.getIotSensorsByAssetIdAndSensorType(assetId, sensorType))
                .build();
    }
    
    @GetMapping("/date-range")
    public ApiResponse<List<IotSensorResponse>> getIotSensorsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {
        return ApiResponse.<List<IotSensorResponse>>builder()
                .result(iotSensorService.getIotSensorsByDateRange(startDate, endDate))
                .build();
    }
    
    @GetMapping("/asset/{assetId}/date-range")
    public ApiResponse<List<IotSensorResponse>> getIotSensorsByAssetIdAndDateRange(
            @PathVariable Long assetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {
        return ApiResponse.<List<IotSensorResponse>>builder()
                .result(iotSensorService.getIotSensorsByAssetIdAndDateRange(assetId, startDate, endDate))
                .build();
    }
    
    @PutMapping("/update/{id}")
    public ApiResponse<IotSensorResponse> updateIotSensor(
            @PathVariable Long id, 
            @RequestBody IotSensorUpdateRequest request) {
        return ApiResponse.<IotSensorResponse>builder()
                .result(iotSensorService.updateIotSensor(id, request))
                .build();
    }
    
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteIotSensor(@PathVariable Long id) {
        iotSensorService.deleteIotSensor(id);
        return ApiResponse.<Void>builder().build();
    }
}
