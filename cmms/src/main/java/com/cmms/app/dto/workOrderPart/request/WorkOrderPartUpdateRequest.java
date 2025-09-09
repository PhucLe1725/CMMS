package com.cmms.app.dto.workOrderPart.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderPartUpdateRequest {
    Long workOrderId;
    Long partId;
    Integer quantityUsed;
}
