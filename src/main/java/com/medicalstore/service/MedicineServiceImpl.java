package com.medicalstore.service;

import com.medicalstore.model.Medicine;
import com.medicalstore.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    @Override
    public Medicine getMedicineById(int id) {
        return medicineRepository.findById(id);
    }

    @Override
    public void addMedicine(Medicine medicine) {
        medicineRepository.save(medicine);
    }

    @Override
    public void updateMedicine(Medicine medicine) {
        medicineRepository.update(medicine);
    }

    @Override
    public void deleteMedicine(int id) {
        medicineRepository.delete(id);
    }
}
