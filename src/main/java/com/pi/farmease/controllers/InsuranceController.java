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


import java.security.Principal;

import java.util.List;


@RestController

@RequestMapping("/api/v1/insurances")

@RequiredArgsConstructor
public class InsuranceController {


    private final InsuranceService insuranceService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        List<Insurance> insurances = insuranceService.getAllInsurances();
        return ResponseEntity.ok(insurances);
    }


    @GetMapping("/insurancesByUser")
    public ResponseEntity<List<Insurance>> getInsurancesByCurrentUser(Principal connectedUser) {
        // Récupérer les assurances de l'utilisateur connecté en utilisant le service InsuranceService
        try {
            List<Insurance> insurances = insuranceService.getInsurancesByCurrentUser(connectedUser);
            return ResponseEntity.ok(insurances);
        } catch (IllegalArgumentException e) {
            // Si aucun utilisateur n'est connecté, retourner une réponse d'erreur 401 (non autorisé)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
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


    @PostMapping("/add/{duration}")
    public ResponseEntity<Insurance> createInsurance(@RequestBody Insurance insurance,@PathVariable int duration , Principal connectedUser) {
        if (connectedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Insurance createdInsurance = insuranceService.saveInsurance(insurance, connectedUser , duration);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdInsurance);
    }

    @PutMapping("/update/{id}")

    public ResponseEntity<Insurance> updateInsurance(@PathVariable("id") Integer id, @RequestBody Insurance insuranceDetails, Principal connectedUser) {
        // Récupérer l'assurance existante par son identifiant
        Insurance existingInsurance = insuranceService.getInsuranceById(id);
        if (existingInsurance != null) {
            // Vérifier si l'utilisateur connecté est autorisé à modifier cette assurance
            try {
                Insurance updatedInsurance = insuranceService.updateInsurance(insuranceDetails, connectedUser);
                return ResponseEntity.ok(updatedInsurance);
            } catch (IllegalArgumentException e) {
                // Si l'utilisateur n'est pas autorisé, retourner une réponse d'erreur
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            // Si l'assurance n'existe pas, retourner une réponse d'erreur 404

            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
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