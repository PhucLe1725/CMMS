package com.cmms.app.dto.inventory.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResponse {
    Long partId;
    Long tenantId;
    String partName;
    String partNumber;
    Integer quantity;
    Integer minQuantity;
    Long locationId;
    BigDecimal unitCost;
    Long supplierId;
}
