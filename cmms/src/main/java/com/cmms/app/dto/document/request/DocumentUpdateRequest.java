package com.cmms.app.dto.document.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUpdateRequest {
    Long documentId;
    Long assetId;
    Long workOrderId;
    String fileName;
    String filePath;
}
