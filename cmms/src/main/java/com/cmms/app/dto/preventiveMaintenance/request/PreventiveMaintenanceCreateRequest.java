package com.cmms.app.dto.preventiveMaintenance.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PreventiveMaintenanceCreateRequest {
    Long assetId;
    String taskDescription;
    String frequencyType;
    Integer frequencyInterval;
    OffsetDateTime nextDueDate;
    OffsetDateTime lastCompleted;
}
