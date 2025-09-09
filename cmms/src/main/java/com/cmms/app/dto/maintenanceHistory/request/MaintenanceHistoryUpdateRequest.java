package com.cmms.app.dto.maintenanceHistory.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaintenanceHistoryUpdateRequest {
    Long historyId;
    Long workOrderId;
    Long assetId;
    String action;
    Long performedBy;
}
