package com.cmms.app.dto.workOrderPart.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderPartCreateRequest {
    Long workOrderId;
    Long partId;
    Integer quantityUsed;
}
