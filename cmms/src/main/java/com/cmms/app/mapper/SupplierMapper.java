package com.cmms.app.mapper;

import com.cmms.app.dto.supplier.request.SupplierCreateRequest;
import com.cmms.app.dto.supplier.request.SupplierUpdateRequest;
import com.cmms.app.dto.supplier.response.SupplierResponse;
import com.cmms.app.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    @Mapping(target = "supplierId", ignore = true)
    Supplier toSupplier(SupplierCreateRequest supplierCreateRequest);

    SupplierResponse toSupplierResponse(Supplier supplier);

    @Mapping(target = "supplierId", ignore = true)
    void updateSupplier(@MappingTarget Supplier supplier, SupplierUpdateRequest supplierUpdateRequest);
}
