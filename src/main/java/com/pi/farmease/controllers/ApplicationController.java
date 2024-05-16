package com.pi.farmease.controllers;

import com.pi.farmease.entities.Application;
import com.pi.farmease.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/all")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }
    @GetMapping("/by-mortgage/{idMortgage}")
    public List<Application> getApplicationsByIdMortgage(@PathVariable long idMortgage) {
        return applicationService.getApplicationsByIdMortgage(idMortgage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable long id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/user")
    public ResponseEntity<List<Application>> getApplicationsByUserId(Principal connected) {
        List<Application> applications = applicationService.getApplicationsByConnectedUser( connected);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }
    @PostMapping("/add/{idmortgage}")
    public ResponseEntity<Void> addApplication(@RequestBody Application application, Principal connected,@PathVariable long idmortgage) {

        applicationService.addApplication(application, connected,idmortgage);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/export")
    public ResponseEntity<Void> exportApplicationToPdf(@PathVariable long id) throws IOException {

        Application application = applicationService.getApplicationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: "+id));

        // Export the application to PDF
        applicationService.exportApplicationToPdf(application, "C:\\Users\\benza\\Desktop\\application_" + id + ".pdf");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateApplication(@PathVariable long id, @RequestBody Application application) {
        applicationService.updateApplication(application, id);
        return ResponseEntity.ok().build();
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/accept/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptApplication(@PathVariable long id) {
        applicationService.accept_application(id);
    }

    @PutMapping("/refuse/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refuseApplication(@PathVariable long id) {
        applicationService.refuse_application(id);
    }
}