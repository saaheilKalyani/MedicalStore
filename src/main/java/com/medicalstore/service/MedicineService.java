package com.medicalstore.service;

import com.medicalstore.model.Medicine;
import java.util.List;

public interface MedicineService {
    List<Medicine> getAllMedicines();
    Medicine getMedicineById(int id);
    void addMedicine(Medicine medicine);
    void updateMedicine(Medicine medicine);
    void deleteMedicine(int id);
}
