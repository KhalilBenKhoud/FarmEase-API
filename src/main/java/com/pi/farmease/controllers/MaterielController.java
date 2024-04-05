package com.pi.farmease.controllers;



import com.pi.farmease.entities.Materiel;
import com.pi.farmease.services.MaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materiels")
public class MaterielController {

    private final MaterielService materielService;

    @Autowired
    public MaterielController(MaterielService materielService) {
        this.materielService = materielService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addMateriel(@RequestBody Materiel materiel) {
        materielService.addMateriel(materiel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materiel> getMaterielById(@PathVariable Long id) {
        Materiel materiel = materielService.getMaterielById(id);
        return materiel != null ?
                new ResponseEntity<>(materiel, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Materiel>> getAllMateriels() {
        List<Materiel> materiels= materielService.getAllMateriels();
        return new ResponseEntity<>(materiels, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateMateriel(@PathVariable Long id, @RequestBody Materiel materiel) {
        materiel.setId_materiel(id);
        materielService.updateMateriel(materiel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMateriel(@PathVariable Long id) {
        materielService.deleteMaterielById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

