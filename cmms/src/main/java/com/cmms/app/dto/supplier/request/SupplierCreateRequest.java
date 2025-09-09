package com.cmms.app.dto.supplier.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierCreateRequest {
    String supplierName;
    String contactInfo;
}
