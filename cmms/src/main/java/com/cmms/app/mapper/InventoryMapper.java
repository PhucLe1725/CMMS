package com.cmms.app.mapper;

import com.cmms.app.dto.inventory.request.InventoryCreateRequest;
import com.cmms.app.dto.inventory.request.InventoryUpdateRequest;
import com.cmms.app.dto.inventory.response.InventoryResponse;
import com.cmms.app.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "partId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Inventory toInventory(InventoryCreateRequest inventoryCreateRequest);

    InventoryResponse toInventoryResponse(Inventory inventory);

    @Mapping(target = "tenantId", ignore = true)
    void updateInventory(@MappingTarget Inventory inventory, InventoryUpdateRequest request);
}
