package com.cmms.app.dto.asset.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetResponse {
    Long assetId;
    Long tenantId;
    String assetName;
    Long assetTypeId;
    String serialNumber;
    Long locationId;
    String status;
    OffsetDateTime purchaseDate;
    OffsetDateTime warrantyExpiry;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}