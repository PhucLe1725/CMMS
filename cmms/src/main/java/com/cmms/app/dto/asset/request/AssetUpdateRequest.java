package com.cmms.app.dto.asset.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetUpdateRequest {
    Long assetId;
    String assetName;
    Long assetTypeId;
    String serialNumber;
    Long locationId;
    String status;
    OffsetDateTime purchaseDate;
    OffsetDateTime warrantyExpiry;
}
