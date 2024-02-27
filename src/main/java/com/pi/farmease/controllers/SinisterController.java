package com.pi.farmease.controllers;

import com.pi.farmease.entities.Sinister;
import com.pi.farmease.services.SinisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sinisters")
@RequiredArgsConstructor
public class SinisterController {

    private final SinisterService sinisterService;

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
}
