package com.cmms.app.dto.workOrder.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderUpdateRequest {
    Long workOrderId;
    Long tenantId;
    Long assetId;
    String title;
    String description;
    String status;
    String priority;
    Long assignedTo;
    Long createdBy;
    OffsetDateTime scheduledDate;
    OffsetDateTime completedAt;
    BigDecimal estimatedHours;
    BigDecimal actualHours;
}
