package com.medicalstore.service;

import com.medicalstore.model.SupplierDto;
import java.util.List;
import java.util.Optional;

public interface SupplierService {
    List<SupplierDto> listAllSuppliers();
    Optional<SupplierDto> findSupplier(int id);
    Integer addSupplier(SupplierDto supplier);
    boolean usernameExists(String username);
    boolean emailExists(String email);
}