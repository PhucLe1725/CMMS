package com.cmms.app.dto.tenant.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantResponse {
    Long tenantId;
    String tenantName;
    String description;
    OffsetDateTime createdAt;
}
