package com.cmms.app.repository;

import com.cmms.app.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s")
    List<Supplier> findAllSuppliers();
    
    @Query(value = "SELECT * FROM suppliers ORDER BY " +
           "CASE WHEN :sortBy = 'supplierName' AND :sortDirection = 'asc' THEN supplier_name END ASC, " +
           "CASE WHEN :sortBy = 'supplierName' AND :sortDirection = 'desc' THEN supplier_name END DESC, " +
           "CASE WHEN :sortBy = 'contactInfo' AND :sortDirection = 'asc' THEN contact_info END ASC, " +
           "CASE WHEN :sortBy = 'contactInfo' AND :sortDirection = 'desc' THEN contact_info END DESC, " +
           "CASE WHEN :sortBy IS NULL OR :sortBy = '' THEN supplier_id END ASC, " +
           "supplier_id ASC " +
           "LIMIT :maxResultCount OFFSET :skipCount", nativeQuery = true)
    List<Supplier> findAllSuppliersWithSortAndLimit(@Param("skipCount") Integer skipCount, 
                                                   @Param("maxResultCount") Integer maxResultCount,
                                                   @Param("sortBy") String sortBy,
                                                   @Param("sortDirection") String sortDirection);
    
    @Query("SELECT COUNT(s) FROM Supplier s")
    long countAll();
}
