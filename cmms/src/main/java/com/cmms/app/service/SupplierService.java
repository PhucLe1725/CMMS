package com.cmms.app.service;

import com.cmms.app.dto.supplier.request.SupplierCreateRequest;
import com.cmms.app.dto.supplier.request.SupplierUpdateRequest;
import com.cmms.app.dto.supplier.response.SupplierResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.entity.Supplier;
import com.cmms.app.mapper.SupplierMapper;
import com.cmms.app.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierMapper supplierMapper;

    public SupplierResponse createSupplier(SupplierCreateRequest request) {
        Supplier supplier = supplierMapper.toSupplier(request);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(supplier);
    }

    public SupplierResponse getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return supplierMapper.toSupplierResponse(supplier);
    }

    public BaseGetAllResponse<SupplierResponse> getAllSuppliers() {
        List<SupplierResponse> supplierResponses = supplierRepository.findAllSuppliers()
                .stream()
                .map(supplierMapper::toSupplierResponse)
                .toList();

        long totalRecords = supplierRepository.countAll();

        return BaseGetAllResponse.<SupplierResponse>builder()
                .data(supplierResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public BaseGetAllResponse<SupplierResponse> getAllSuppliers(Integer skipCount, Integer maxResultCount, String sortBy, String sortDirection) {
        List<Supplier> suppliers = supplierRepository.findAllSuppliersWithSortAndLimit(skipCount, maxResultCount, sortBy, sortDirection);
        long totalRecords = supplierRepository.countAll();
        
        List<SupplierResponse> supplierResponses = suppliers.stream()
                .map(supplierMapper::toSupplierResponse)
                .toList();

        return BaseGetAllResponse.<SupplierResponse>builder()
                .data(supplierResponses)
                .totalRecords(totalRecords)
                .build();
    }

    public SupplierResponse updateSupplier(SupplierUpdateRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplierMapper.updateSupplier(supplier, request);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(supplier);
    }

    public void deleteSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplierRepository.delete(supplier);
    }
}
