package com.pi.farmease.services;

import com.pi.farmease.entities.Investment;
import com.pi.farmease.entities.Project;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface InvestmentService {

    Investment createInvestment(Investment requestBody,Long projectId, Principal connected);

    Optional<Investment> getInvestmentById(Long id);

    List<Investment> getAllInvestments();

    List<Investment> getInvestmentsByProject(Long projectId);

    List<Investment> getInvestmentsByInvestor(Integer investorId);

    Investment updateInvestment(Investment investment);

    void deleteInvestmentById(Long id);

    boolean existsById(Long id);

    void sendInvestmentNotification(String to, String subject, String body);
    Double calculateOwnershipStake(Investment investment);

}
