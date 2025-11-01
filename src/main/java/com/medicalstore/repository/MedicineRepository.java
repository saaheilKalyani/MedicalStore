package com.medicalstore.repository;

import com.medicalstore.model.Medicine;
import java.util.List;

public interface MedicineRepository {
    List<Medicine> findAll();
    Medicine findById(int id);
    int save(Medicine medicine);
    int update(Medicine medicine);
    int delete(int id);
}
