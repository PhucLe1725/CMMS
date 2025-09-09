package com.cmms.app.dto.report.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportUpdateRequest {
    Long reportId;
    Long tenantId;
    String reportType;
    String data;
    Long createdBy;
}
