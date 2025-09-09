package com.cmms.app.dto.tenant.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantGetAllRequest {
    Long tenantId;
    String tenantName;
    String description;
    OffsetDateTime createdAt;
}
