package com.pi.farmease.controllers;

import com.pi.farmease.entities.Ground;
import com.pi.farmease.services.GroundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grounds")
public class GroundController {

    private final GroundService groundService;


    public GroundController(GroundService groundService) {
        this.groundService = groundService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addGround(@RequestBody Ground ground) {
        groundService.addGround(ground);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ground> getGroundById(@PathVariable Long id) {
        Ground ground = groundService.getGroundById(id);
        return ground != null ?
                new ResponseEntity<>(ground, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ground>> getAllGrounds() {
        List<Ground> grounds = groundService.getAllGrounds();
        return new ResponseEntity<>(grounds, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateGround(@PathVariable Long id, @RequestBody Ground ground) {
        ground.setId_ground(id);
        groundService.updateGround(ground);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGround(@PathVariable Long id) {
        groundService.deleteGroundById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
