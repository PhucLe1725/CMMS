package com.cmms.app.dto.report.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportCreateRequest {
    Long tenantId;
    String reportType;
    String data;
    Long createdBy;
}
