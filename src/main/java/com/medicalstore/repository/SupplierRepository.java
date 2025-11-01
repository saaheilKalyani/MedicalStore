package com.medicalstore.repository;

import com.medicalstore.model.SupplierDto;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
    List<SupplierDto> findAllSuppliers();
    Optional<SupplierDto> findById(int userId);
    Optional<SupplierDto> findByUsername(String username);
    Optional<SupplierDto> findByEmail(String email);
    Integer saveSupplier(SupplierDto supplier); // returns generated user ID
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}