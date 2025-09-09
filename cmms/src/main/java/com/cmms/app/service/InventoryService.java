package com.cmms.app.service;

import com.cmms.app.dto.inventory.request.InventoryCreateRequest;
import com.cmms.app.dto.inventory.request.InventoryUpdateRequest;
import com.cmms.app.dto.inventory.response.InventoryResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.Inventory;
import com.cmms.app.mapper.InventoryMapper;
import com.cmms.app.repository.InventoryRepository;
import com.cmms.app.repository.LocationRepository;
import com.cmms.app.repository.SupplierRepository;
import com.cmms.app.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public InventoryResponse createInventory(InventoryCreateRequest inventoryCreateRequest, Long tenantId) {
        // Validate tenant exists
        if (!tenantRepository.existsById(tenantId)) {
            throw new RuntimeException("Tenant not found");
        }

        // Validate location exists
        if (!locationRepository.existsById(inventoryCreateRequest.getLocationId())) {
            throw new RuntimeException("Location not found");
        }

        // Validate supplier exists (if provided)
        if (inventoryCreateRequest.getSupplierId() != null && 
            !supplierRepository.existsById(inventoryCreateRequest.getSupplierId())) {
            throw new RuntimeException("Supplier not found");
        }

        // Check if part number already exists
        if (inventoryRepository.existsByPartNumber(inventoryCreateRequest.getPartNumber())) {
            throw new RuntimeException("Part number already exists");
        }

        Inventory inventory = inventoryMapper.toInventory(inventoryCreateRequest);
        inventory.setTenantId(tenantId);
        
        Inventory savedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.toInventoryResponse(savedInventory);
    }

    public InventoryResponse getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return inventoryMapper.toInventoryResponse(inventory);
    }

    public BaseGetAllResponse<InventoryResponse> getAllInventoriesByTenantId(Long tenantId) {
        List<Inventory> inventories = inventoryRepository.findByTenantId(tenantId);
        
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(inventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());

        long totalRecords = inventoryRepository.countByTenantId(tenantId);

        return BaseGetAllResponse.<InventoryResponse>builder()
                .data(inventoryResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public BaseGetAllResponse<InventoryResponse> getAllInventoriesByTenantId(Long tenantId, Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<Inventory> inventories = inventoryRepository.findAllInventoriesByTenantIdWithSortAndLimit(tenantId, skipCount, maxResultCount, sortBy, sortDirection);
        long totalRecords = inventoryRepository.countByTenantId(tenantId);
        
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(inventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());

        return BaseGetAllResponse.<InventoryResponse>builder()
                .data(inventoryResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public InventoryResponse updateInventory(InventoryUpdateRequest inventoryUpdateRequest) {
        Inventory existingInventory = inventoryRepository.findById(inventoryUpdateRequest.getPartId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        // Validate location exists
        if (!locationRepository.existsById(inventoryUpdateRequest.getLocationId())) {
            throw new RuntimeException("Location not found");
        }

        // Validate supplier exists (if provided)
        if (inventoryUpdateRequest.getSupplierId() != null && 
            !supplierRepository.existsById(inventoryUpdateRequest.getSupplierId())) {
            throw new RuntimeException("Supplier not found");
        }

        // Check if part number already exists for different inventory
        if (!existingInventory.getPartNumber().equals(inventoryUpdateRequest.getPartNumber()) &&
            inventoryRepository.existsByPartNumber(inventoryUpdateRequest.getPartNumber())) {
            throw new RuntimeException("Part number already exists");
        }

        inventoryMapper.updateInventory(existingInventory, inventoryUpdateRequest);
        
        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return inventoryMapper.toInventoryResponse(updatedInventory);
    }

    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new RuntimeException("Inventory not found");
        }
        inventoryRepository.deleteById(id);
    }

    public BaseGetAllResponse<InventoryResponse> getLowStockInventories(Long tenantId) {
        List<Inventory> inventories = inventoryRepository.findByQuantityLessThanMinQuantity()
                .stream()
                .filter(inventory -> inventory.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
        
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(inventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());

        long totalRecords = inventoryRepository.countLowStockByTenantId(tenantId);

        return BaseGetAllResponse.<InventoryResponse>builder()
                .data(inventoryResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public BaseGetAllResponse<InventoryResponse> getLowStockInventories(Long tenantId, Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<Inventory> inventories = inventoryRepository.findLowStockInventoriesByTenantIdWithSortAndLimit(tenantId, skipCount, maxResultCount, sortBy, sortDirection);
        long totalRecords = inventoryRepository.countLowStockByTenantId(tenantId);
        
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(inventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());

        return BaseGetAllResponse.<InventoryResponse>builder()
                .data(inventoryResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public BaseGetAllResponse<InventoryResponse> getInventoriesByLocation(Long locationId, Long tenantId) {
        List<Inventory> inventories = inventoryRepository.findByLocationId(locationId)
                .stream()
                .filter(inventory -> inventory.getTenantId().equals(tenantId))
                .collect(Collectors.toList());
        
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(inventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());

        long totalRecords = inventoryRepository.countByTenantIdAndLocationId(tenantId, locationId);

        return BaseGetAllResponse.<InventoryResponse>builder()
                .data(inventoryResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public BaseGetAllResponse<InventoryResponse> getInventoriesByLocation(Long locationId, Long tenantId, Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<Inventory> inventories = inventoryRepository.findInventoriesByLocationWithSortAndLimit(tenantId, locationId, skipCount, maxResultCount, sortBy, sortDirection);
        long totalRecords = inventoryRepository.countByTenantIdAndLocationId(tenantId, locationId);
        
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(inventoryMapper::toInventoryResponse)
                .collect(Collectors.toList());

        return BaseGetAllResponse.<InventoryResponse>builder()
                .data(inventoryResponses)
                .totalRecords(totalRecords)
                .build();
    }
}
