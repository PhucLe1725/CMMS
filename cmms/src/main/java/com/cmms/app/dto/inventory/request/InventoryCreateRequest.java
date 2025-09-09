package com.cmms.app.dto.inventory.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCreateRequest {
    String partName;
    String partNumber;
    Integer quantity;
    Integer minQuantity;
    Long locationId;
    BigDecimal unitCost;
    Long supplierId;
}
