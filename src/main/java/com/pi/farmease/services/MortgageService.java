package com.pi.farmease.services;

import com.pi.farmease.dto.requests.MortgageRequest;
import com.pi.farmease.entities.Ground;
import com.pi.farmease.entities.Materiel;
import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.entities.Post;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface MortgageService
{ public void addMortgage(MortgageRequest mortgage);

    List<Mortgage> getAllMortgages();
    void updateMortgageRating(Long id, double rate);
    Optional<Mortgage> getMortgageById(Long id);

    Mortgage updateMortgage(Long id, Mortgage mortgageDetails);
    List<Materiel> getAllMateriels();
    List<Ground> getAllGroundsplace() ;
    void deleteMortgage(Long id);
    void postOnLinkedIn(String shareCommentary);
}
