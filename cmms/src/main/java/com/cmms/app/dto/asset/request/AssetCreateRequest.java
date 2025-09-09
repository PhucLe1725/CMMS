package com.cmms.app.dto.asset.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssetCreateRequest {
    String assetName;
    Long assetTypeId;
    String serialNumber;
    Long locationId;
    String status;
    OffsetDateTime purchaseDate;
    OffsetDateTime warrantyExpiry;
}
