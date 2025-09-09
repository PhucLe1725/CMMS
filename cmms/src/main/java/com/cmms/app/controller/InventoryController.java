package com.cmms.app.controller;

import com.cmms.app.dto.inventory.request.InventoryCreateRequest;
import com.cmms.app.dto.inventory.request.InventoryGetAllRequest;
import com.cmms.app.dto.inventory.request.InventoryUpdateRequest;
import com.cmms.app.dto.inventory.response.InventoryResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/create")
    public ApiResponse<InventoryResponse> createInventory(
            @RequestBody InventoryCreateRequest inventoryCreateRequest, 
            @RequestParam Long tenantId) {
        return ApiResponse.<InventoryResponse>builder()
                .result(inventoryService.createInventory(inventoryCreateRequest, tenantId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<InventoryResponse> getInventoryById(@PathVariable Long id) {
        return ApiResponse.<InventoryResponse>builder()
                .result(inventoryService.getInventoryById(id))
                .build();
    }

    @GetMapping("/getAllByTenantId")
    public ApiResponse<BaseGetAllResponse<InventoryResponse>> getAllInventoriesByTenantId(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        InventoryGetAllRequest request = new InventoryGetAllRequest();
        request.setTenantId(tenantId);
        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setSortBy(sortBy);
        request.setSortDirection(sortDirection);

        return ApiResponse.<BaseGetAllResponse<InventoryResponse>>builder()
                .result(inventoryService.getAllInventoriesByTenantId(request.getTenantId(), request.getSkipCount(), request.getMaxResultCount(), request.getSortBy(), request.getSortDirection()))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<InventoryResponse> updateInventory(
            @PathVariable Long id, 
            @RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
        inventoryUpdateRequest.setPartId(id);
        return ApiResponse.<InventoryResponse>builder()
                .result(inventoryService.updateInventory(inventoryUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

    @GetMapping("/low-stock")
    public ApiResponse<BaseGetAllResponse<InventoryResponse>> getLowStockInventories(
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ApiResponse.<BaseGetAllResponse<InventoryResponse>>builder()
                .result(inventoryService.getLowStockInventories(tenantId, skipCount, maxResultCount, sortBy, sortDirection))
                .build();
    }

    @GetMapping("/by-location")
    public ApiResponse<BaseGetAllResponse<InventoryResponse>> getInventoriesByLocation(
            @RequestParam Long locationId,
            @RequestParam Long tenantId,
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ApiResponse.<BaseGetAllResponse<InventoryResponse>>builder()
                .result(inventoryService.getInventoriesByLocation(locationId, tenantId, skipCount, maxResultCount, sortBy, sortDirection))
                .build();
    }
}
