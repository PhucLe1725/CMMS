package com.cmms.app.dto.supplier.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierResponse {
    Long supplierId;
    String supplierName;
    String contactInfo;
}
