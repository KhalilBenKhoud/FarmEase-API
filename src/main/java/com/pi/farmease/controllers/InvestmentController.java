package com.pi.farmease.controllers;

import com.pi.farmease.exceptions.BusinessException;
import com.pi.farmease.exceptions.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pi.farmease.entities.Investment;
import com.pi.farmease.services.InvestmentService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/investments")
@AllArgsConstructor
public class InvestmentController {

    private InvestmentService investmentService;

    @PostMapping
    public ResponseEntity<Investment> createInvestment(@RequestBody Investment requestBody, Principal connected) {
        try {
            Investment savedInvestment = investmentService.createInvestment(requestBody, connected);
            return new ResponseEntity<>(savedInvestment, HttpStatus.CREATED);
        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investment> getInvestmentById(@PathVariable Long id) {
        Optional<Investment> investment = investmentService.getInvestmentById(id);
        return investment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Investment>> getAllInvestments() {
        List<Investment> investments = investmentService.getAllInvestments();
        return new ResponseEntity<>(investments, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Investment>> getInvestmentsByProject(@PathVariable Long projectId) {
        List<Investment> investments = investmentService.getInvestmentsByProject(projectId);
        return new ResponseEntity<>(investments, HttpStatus.OK);
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<Investment>> getInvestmentsByInvestor(@PathVariable Integer investorId) {
        List<Investment> investments = investmentService.getInvestmentsByInvestor(investorId);
        return new ResponseEntity<>(investments, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investment> updateInvestment(@PathVariable Long id, @Valid @RequestBody Investment investment) {
        if (!id.equals(investment.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Investment updatedInvestment = investmentService.updateInvestment(investment);
        return new ResponseEntity<>(updatedInvestment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long id) {
        investmentService.deleteInvestmentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
