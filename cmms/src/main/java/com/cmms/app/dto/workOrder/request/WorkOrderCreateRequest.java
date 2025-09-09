package com.cmms.app.dto.workOrder.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderCreateRequest {
    Long tenantId;
    Long assetId;
    String title;
    String description;
    String status;
    String priority;
    Long assignedTo;
    Long createdBy;
    OffsetDateTime scheduledDate;
    BigDecimal estimatedHours;
    BigDecimal actualHours;
}
