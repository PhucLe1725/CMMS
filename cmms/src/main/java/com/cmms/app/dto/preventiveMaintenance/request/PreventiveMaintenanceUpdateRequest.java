package com.cmms.app.dto.preventiveMaintenance.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PreventiveMaintenanceUpdateRequest {
    Long pmId;
    Long assetId;
    String taskDescription;
    String frequencyType;
    Integer frequencyInterval;
    OffsetDateTime nextDueDate;
    OffsetDateTime lastCompleted;
}
