package com.cmms.app.dto.document.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentCreateRequest {
    Long assetId;
    Long workOrderId;
    String fileName;
    String filePath;
}
