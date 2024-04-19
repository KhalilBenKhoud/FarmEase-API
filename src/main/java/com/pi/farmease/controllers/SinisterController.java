package com.pi.farmease.controllers;

import com.pi.farmease.dao.SinisterRepository;
import com.pi.farmease.entities.Sinister;
import com.pi.farmease.services.SinisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sinisters")
@RequiredArgsConstructor
public class SinisterController {

    private final SinisterService sinisterService;
    private final SinisterRepository sinisterRepository;

    @GetMapping
    public ResponseEntity<List<Sinister>> getAllSinisters() {
        List<Sinister> sinisters = sinisterService.getAllSinisters();
        return ResponseEntity.ok(sinisters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sinister> getSinisterById(@PathVariable("id") Integer id) {
        Sinister sinister = sinisterService.getSinisterById(id);
        if (sinister != null) {
            return ResponseEntity.ok(sinister);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Sinister> createSinister(@RequestBody Sinister sinister) {
        Sinister createdSinister = sinisterService.saveSinister(sinister);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSinister);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sinister> updateSinister(@PathVariable("id") Integer id, @RequestBody Sinister sinisterDetails) {
        Sinister existingSinister = sinisterService.getSinisterById(id);
        if (existingSinister != null) {
            sinisterDetails.setId(id);
            Sinister updatedSinister = sinisterService.updateSinister(sinisterDetails);
            return ResponseEntity.ok(updatedSinister);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSinister(@PathVariable("id") Integer id) {
        Sinister existingSinister = sinisterService.getSinisterById(id);
        if (existingSinister != null) {
            sinisterService.deleteSinister(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, @PathVariable("id") Integer id) {
        // Check if file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file");
        }
        try {
            String fileName = sinisterService.savePhoto(file);
            Sinister s=sinisterService.getSinisterById(id);
            s.setPhoto(fileName);
            sinisterService.updateSinister(s);
            return ResponseEntity.ok().body("Sinister photo uploaded successfully: "
                    + s.getDescription()
                    +", your file name is: "
                    +s.getPhoto());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload picture");
        }
    }

    @GetMapping("/date/{month}")
    public ResponseEntity<String> getSinistersStatisticsByDate(@PathVariable("month") int month) {
        List<Sinister> sinisters = sinisterRepository.getSinistersByDate_Sinister(month);
        Double totalAmount = sinisterRepository.getTotalAmountByMonth(month);
        if (!sinisters.isEmpty()) {
            String message = String.format("Le nombre de sinistres du mois %d est %d, et la somme des montants est %.2f",
                    month, sinisters.size(), totalAmount != null ? totalAmount : 0.0);
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
