package com.cmms.app.dto.supplier.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierUpdateRequest {
    Long supplierId;
    String supplierName;
    String contactInfo;
}
