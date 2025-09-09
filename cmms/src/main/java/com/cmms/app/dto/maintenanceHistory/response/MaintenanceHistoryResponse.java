package com.cmms.app.dto.maintenanceHistory.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaintenanceHistoryResponse {
    Long historyId;
    Long workOrderId;
    Long assetId;
    String action;
    Long performedBy;
    OffsetDateTime performedAt;
}
