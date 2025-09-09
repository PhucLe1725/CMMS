package com.cmms.app.dto.workOrder.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderResponse {
    Long workOrderId;
    Long tenantId;
    Long assetId;
    String title;
    String description;
    String status;
    String priority;
    Long assignedTo;
    Long createdBy;
    OffsetDateTime createdAt;
    OffsetDateTime scheduledDate;
    OffsetDateTime completedAt;
    BigDecimal estimatedHours;
    BigDecimal actualHours;
}
