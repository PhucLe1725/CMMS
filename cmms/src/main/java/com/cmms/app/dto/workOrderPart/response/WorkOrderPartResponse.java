package com.cmms.app.dto.workOrderPart.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderPartResponse {
    Long workOrderId;
    Long partId;
    Integer quantityUsed;
}
