package com.pi.farmease.controllers;

import com.pi.farmease.dto.requests.MortgageRequest;
import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.Ground;
import com.pi.farmease.entities.Materiel;
import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.services.ApplicationService;
import com.pi.farmease.services.MaterielService;
import com.pi.farmease.services.MortgageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/mortgages")
@RequiredArgsConstructor
public class MortgageController {
    private final MortgageService mortgageService;
private final ApplicationService applicationservice;
private final MaterielService materielservice;

    @GetMapping("/get")
    public List<Mortgage> getAllMortgages() {
        return mortgageService.getAllMortgages();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMortgageById(@PathVariable Long id) {
        return mortgageService.getMortgageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/update-rating/{id}/{rate}")
    public ResponseEntity<?> updateMortgageRating(@PathVariable Long id, @PathVariable double rate) {
        mortgageService.updateMortgageRating(id, rate);
        return ResponseEntity.ok().body("Mortgage rating updated with ID: " + id);
    }
    @GetMapping("/places")
    public List<Ground> getAllGrounds() {
        return mortgageService.getAllGroundsplace();
    }
    @GetMapping("/materiel")
    public List<Materiel> getAllMateriel() {
        return mortgageService.getAllMateriels();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMortgage(@RequestBody MortgageRequest mortgage) {
         mortgageService.addMortgage(mortgage);
        return ResponseEntity.ok().body("Mortgage added with ID: ");
    }
    @PostMapping("/addmateriel")
    public ResponseEntity<Void> addMateriel(@RequestBody Materiel materiel) {
        materielservice.addMateriel(materiel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMortgage(@PathVariable Long id, @RequestBody Mortgage mortgage) {
        Mortgage updatedMortgage = mortgageService.updateMortgage(id, mortgage);
        return ResponseEntity.ok().body("Mortgage updated with ID: " + updatedMortgage.getId_mortgage());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMortgage(@PathVariable Long id) {
        applicationservice.deleteApplicationBymortgage(id);

        mortgageService.deleteMortgage(id);
        return ResponseEntity.ok().body("Mortgage deleted with ID: " + id);
    }
}
