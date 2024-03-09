package com.pi.farmease.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pi.farmease.entities.Project;
import com.pi.farmease.services.PerformanceService;
import com.pi.farmease.services.ProjectService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
public class ProjectController {

    private ProjectService projectService;
    private PerformanceService performanceService;

    // Get all projects (paginated)
    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(Pageable pageable) {
        Page<Project> projects = projectService.findAllProjects(pageable);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Get all projects created by a specific user (paginated)
    @GetMapping("/by-creator/{userId}")
    public ResponseEntity<Page<Project>> getProjectsByCreator(@PathVariable Long userId, Pageable pageable) {
        Page<Project> projects = projectService.findProjectsByCreator(userId, pageable);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Get a project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project, Principal connected) {
        Project savedProject = projectService.createProject(project, connected);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    // Update a project
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @Valid @RequestBody Project project) {
        project.setId(id); // Ensure updating existing project with correct ID
        Project updatedProject = projectService.updateProject(project);
        if (updatedProject != null) {
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a project by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Indicate successful deletion without body content
    }

    // Get basic performance for a project
    @GetMapping("/{id}/performance")
    public ResponseEntity<Map<String, Double>> getBasicPerformance(@PathVariable Long id) {
        Project project = projectService.getProjectById(id).orElse(null);
        if (project != null) {
            Map<String, Double> performanceMetrics = performanceService.calculateBasicPerformance(project);
            return new ResponseEntity<>(performanceMetrics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Check if a project exists by ID
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> projectExistsById(@PathVariable Long id) {
        boolean exists = projectService.existsById(id);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
