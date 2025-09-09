package com.cmms.app.dto.maintenanceHistory.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaintenanceHistoryCreateRequest {
    Long workOrderId;
    Long assetId;
    String action;
    Long performedBy;
}
