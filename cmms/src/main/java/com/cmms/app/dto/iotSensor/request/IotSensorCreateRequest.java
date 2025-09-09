package com.cmms.app.dto.iotSensor.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IotSensorCreateRequest {
    Long assetId;
    String sensorType;
    String dataValue;
}
