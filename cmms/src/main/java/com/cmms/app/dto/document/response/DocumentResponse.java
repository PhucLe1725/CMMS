package com.cmms.app.dto.document.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentResponse {
    Long documentId;
    Long assetId;
    Long workOrderId;
    String fileName;
    String filePath;
    OffsetDateTime uploadedAt;
}
