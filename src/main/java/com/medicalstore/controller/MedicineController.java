package com.medicalstore.controller;

import com.medicalstore.model.Medicine;
import com.medicalstore.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/medicines")
    public String listMedicines(Model model) {
        model.addAttribute("medicines", medicineService.getAllMedicines());
        return "medicines";
    }

    @GetMapping("/medicine/add")
    public String addMedicineForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "addMedicine";
    }

    @PostMapping("/medicine/add")
    public String saveMedicine(@ModelAttribute("medicine") Medicine medicine) {
        medicineService.addMedicine(medicine);
        return "redirect:/medicines";
    }

    @GetMapping("/medicine/edit/{id}")
    public String editMedicineForm(@PathVariable("id") int id, Model model) {
        Medicine medicine = medicineService.getMedicineById(id);
        model.addAttribute("medicine", medicine);
        return "editMedicine";
    }

    @PostMapping("/medicine/update/{id}")
    public String updateMedicine(@PathVariable("id") int id, @ModelAttribute("medicine") Medicine medicine) {
        medicine.setMedicineId(id);
        medicineService.updateMedicine(medicine);
        return "redirect:/medicines";
    }

    @PostMapping("/medicine/delete/{id}")
    public String deleteMedicine(@PathVariable("id") int id) {
        medicineService.deleteMedicine(id);
        return "redirect:/medicines";
    }
}
