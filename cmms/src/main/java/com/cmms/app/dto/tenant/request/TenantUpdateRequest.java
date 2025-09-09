package com.cmms.app.dto.tenant.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantUpdateRequest {
    Long tenantId;
    String tenantName;
    String description;
}
