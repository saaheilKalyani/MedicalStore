package com.medicalstore.service.impl;

import com.medicalstore.model.SupplierDto;
import com.medicalstore.repository.SupplierRepository;
import com.medicalstore.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repo;

    public SupplierServiceImpl(SupplierRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<SupplierDto> listAllSuppliers() {
        return repo.findAllSuppliers();
    }

    @Override
    public Optional<SupplierDto> findSupplier(int id) {
        return repo.findById(id);
    }

    @Override
    public Integer addSupplier(SupplierDto supplier) {
        // Validate unique constraints
        if (usernameExists(supplier.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + supplier.getUsername());
        }
        if (emailExists(supplier.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + supplier.getEmail());
        }

        return repo.saveSupplier(supplier);
    }

    @Override
    public boolean usernameExists(String username) {
        return repo.existsByUsername(username);
    }

    @Override
    public boolean emailExists(String email) {
        return repo.existsByEmail(email);
    }
}