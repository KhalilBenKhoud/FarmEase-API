package com.pi.farmease.services;

import com.pi.farmease.entities.Insurance;
import com.pi.farmease.dao.InsuranceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InsuranceServiceImp {

    private final InsuranceRepository insuranceRepository;


    // Méthode pour récupérer toutes les assurances
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    // Méthode pour récupérer une assurance par son ID
    public Insurance getInsuranceById(Integer id) {
        return insuranceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));
    }

    // Méthode pour enregistrer une assurance
    public Insurance saveInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    // Méthode pour mettre à jour une assurance
    public Insurance updateInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    // Méthode pour supprimer une assurance
    public void deleteInsurance(Integer id) {
        insuranceRepository.deleteById(id);
    }
}