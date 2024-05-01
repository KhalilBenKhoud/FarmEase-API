package com.pi.farmease.services;

import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.entities.Post;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface MortgageService
{ public void addMortgage(Mortgage mortgage);

    List<Mortgage> getAllMortgages();
    void updateMortgageRating(Long id, double rate);
    Optional<Mortgage> getMortgageById(Long id);

    Mortgage updateMortgage(Long id, Mortgage mortgageDetails);

    void deleteMortgage(Long id);
    void postOnLinkedIn(String shareCommentary);
}
