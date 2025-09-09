package com.cmms.app.dto.report.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    Long reportId;
    Long tenantId;
    String reportType;
    OffsetDateTime generatedAt;
    String data;
    Long createdBy;
}
