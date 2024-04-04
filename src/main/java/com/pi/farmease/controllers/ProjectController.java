package com.pi.farmease.controllers;

import com.pi.farmease.entities.enumerations.ProjectCategory;
import com.pi.farmease.entities.enumerations.ProjectStatus;
import com.pi.farmease.services.PdfService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.pi.farmease.entities.Project;
import com.pi.farmease.services.PerformanceService;
import com.pi.farmease.services.ProjectService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
public class ProjectController {

    private ProjectService projectService;
    private PerformanceService performanceService;
    private final PdfService pdfService;


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
    @PostMapping("/createProject")
    public ResponseEntity<Project> createProject(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Double netIncomeLastYear,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageUrl,
            @RequestParam String address,
            @RequestParam Double goalAmount,
            @RequestParam Date deadline,
            @RequestParam Double equityOffered,
            @RequestParam Double dividendPayoutRatio,
            @RequestParam Double totalInvestment,
            @RequestParam ProjectCategory projectCategory,
            Principal connected) throws IOException {

        Project project = Project.builder()
                .title(title)
                .description(description)
                .netIncomeLastYear(netIncomeLastYear)
                .address(address)
                .goalAmount(goalAmount)
                .deadline(deadline)
                .equityOffered(equityOffered)
                .dividendPayoutRatio(dividendPayoutRatio)
                .totalInvestment(totalInvestment)
                .projectCategory(projectCategory)
                .projectStatus(ProjectStatus.PENDING) // Set default status
                .createdAt(new Date())
                .build();

            Project savedProject = projectService.createProject(project,imageUrl,connected);



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

    // Check if a project exists by ID
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> projectExistsById(@PathVariable Long id) {
        boolean exists = projectService.existsById(id);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @GetMapping("/{projectId}/pdf")
    public ResponseEntity<byte[]> generatePdfForProject(@PathVariable Long projectId) {
        Optional<Project> optionalProject = projectService.getProjectById(projectId);
        if (optionalProject.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Project project = optionalProject.get();
        try {
            byte[] pdfBytes = pdfService.generatePdf(project);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "project_report.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
