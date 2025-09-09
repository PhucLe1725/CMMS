package com.cmms.app.dto.assetType.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetTypeGetAllRequest {
    Long assetTypeId;
    String typeName;
    String description;
}
