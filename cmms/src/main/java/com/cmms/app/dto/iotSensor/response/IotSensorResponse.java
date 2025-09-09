package com.cmms.app.dto.iotSensor.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IotSensorResponse {
    Long sensorId;
    Long assetId;
    String sensorType;
    String dataValue;
    OffsetDateTime recordedAt;
}
