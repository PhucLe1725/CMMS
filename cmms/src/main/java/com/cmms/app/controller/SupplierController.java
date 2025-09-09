package com.cmms.app.controller;

import com.cmms.app.dto.supplier.request.SupplierCreateRequest;
import com.cmms.app.dto.supplier.request.SupplierGetAllRequest;
import com.cmms.app.dto.supplier.request.SupplierUpdateRequest;
import com.cmms.app.dto.supplier.response.SupplierResponse;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/create")
    public ApiResponse<SupplierResponse> createSupplier(@RequestBody SupplierCreateRequest supplierCreateRequest) {
        return ApiResponse.<SupplierResponse>builder()
                .result(supplierService.createSupplier(supplierCreateRequest))
                .build();
    }

    @GetMapping("/getAll")
    public ApiResponse<BaseGetAllResponse<SupplierResponse>> getAllSuppliers(
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        SupplierGetAllRequest request = new SupplierGetAllRequest();
        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setSortBy(sortBy);
        request.setSortDirection(sortDirection);

        return ApiResponse.<BaseGetAllResponse<SupplierResponse>>builder()
                .result(supplierService.getAllSuppliers(request.getSkipCount(), request.getMaxResultCount(), request.getSortBy(), request.getSortDirection()))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierResponse> getSupplierById(@PathVariable Long id) {
        return ApiResponse.<SupplierResponse>builder()
                .result(supplierService.getSupplierById(id))
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<SupplierResponse> updateSupplier(@PathVariable Long id, @RequestBody SupplierUpdateRequest supplierUpdateRequest) {
        supplierUpdateRequest.setSupplierId(id);
        return ApiResponse.<SupplierResponse>builder()
                .result(supplierService.updateSupplier(supplierUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
