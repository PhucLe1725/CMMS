package com.cmms.app.dto.assetType.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetTypeCreateRequest {
    String typeName;
    String description;
}
