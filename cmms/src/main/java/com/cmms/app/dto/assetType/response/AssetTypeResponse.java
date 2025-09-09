package com.cmms.app.dto.assetType.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetTypeResponse {
    Long assetTypeId;
    String typeName;
    String description;
}
