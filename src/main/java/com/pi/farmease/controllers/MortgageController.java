package com.pi.farmease.controllers;

import com.pi.farmease.dto.responses.MessageResponse;
import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.services.MortgageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/mortgages")
@RequiredArgsConstructor
public class MortgageController {
    private final MortgageService mortgageService;


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
    @PostMapping("/add")
    public ResponseEntity<?> addMortgage(@RequestBody Mortgage mortgage) {
         mortgageService.addMortgage(mortgage);
        return ResponseEntity.ok().body("Mortgage added with ID: " + mortgage.getId_mortgage());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMortgage(@PathVariable Long id, @RequestBody Mortgage mortgage) {
        Mortgage updatedMortgage = mortgageService.updateMortgage(id, mortgage);
        return ResponseEntity.ok().body("Mortgage updated with ID: " + updatedMortgage.getId_mortgage());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMortgage(@PathVariable Long id) {
        mortgageService.deleteMortgage(id);
        return ResponseEntity.ok().body("Mortgage deleted with ID: " + id);
    }
}
