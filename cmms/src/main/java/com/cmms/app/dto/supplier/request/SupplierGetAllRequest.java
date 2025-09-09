package com.cmms.app.dto.supplier.request;

import com.cmms.app.dto.base.request.BaseGetAllRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierGetAllRequest extends BaseGetAllRequest {
    String supplierName;
}
