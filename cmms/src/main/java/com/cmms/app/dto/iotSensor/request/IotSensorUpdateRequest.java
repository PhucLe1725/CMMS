package com.cmms.app.dto.iotSensor.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IotSensorUpdateRequest {
    Long sensorId;
    Long assetId;
    String sensorType;
    String dataValue;
}
