package com.pi.farmease.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pi.farmease.entities.Performance;
import com.pi.farmease.entities.Project;
import com.pi.farmease.services.PerformanceService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/performance")
@AllArgsConstructor
public class PerformanceController {

    private PerformanceService performanceService;


    @GetMapping("/{projectId}")
    public ResponseEntity<Performance> getPerformanceByProject(@PathVariable Long projectId) {
        Optional<Performance> performance = performanceService.getPerformanceByProject(projectId);
        if (performance.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(performance.get(), HttpStatus.OK);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Performance> updatePerformance(@PathVariable Long projectId,  @RequestBody Performance performance) {
        if (!projectId.equals(performance.getProject().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        performance.setProject(new Project()); // Update project association
        Performance updatedPerformance = performanceService.updatePerformance(performance);
        return new ResponseEntity<>(updatedPerformance, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deletePerformanceByProject(@PathVariable Long projectId) {
        performanceService.deletePerformanceByProject(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
