package com.pi.farmease.controllers;

import com.pi.farmease.entities.Insurance;
import com.pi.farmease.services.InsuranceService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/insurances")
@RequiredArgsConstructor
public class InsuranceController {


    private final InsuranceService insuranceService;

    @GetMapping
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        List<Insurance> insurances = insuranceService.getAllInsurances();
        return ResponseEntity.ok(insurances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable("id") Integer id) {
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance != null) {
            return ResponseEntity.ok(insurance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Insurance> createInsurance(@RequestBody Insurance insurance) {
        Insurance createdInsurance = insuranceService.saveInsurance(insurance);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInsurance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(@PathVariable("id") Integer id, @RequestBody Insurance insuranceDetails) {
        Insurance existingInsurance = insuranceService.getInsuranceById(id);
        if (existingInsurance != null) {
            insuranceDetails.setId(id);
            Insurance updatedInsurance = insuranceService.updateInsurance(insuranceDetails);
            return ResponseEntity.ok(updatedInsurance);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable("id") Integer id) {
        Insurance existingInsurance = insuranceService.getInsuranceById(id);
        if (existingInsurance != null) {
            insuranceService.deleteInsurance(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}