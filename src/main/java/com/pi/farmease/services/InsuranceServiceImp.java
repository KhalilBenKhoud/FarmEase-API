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
public class InsuranceServiceImp implements InsuranceService{

    private final InsuranceRepository insuranceRepository;
    private final IEmailService emailService;


    // Méthode pour récupérer toutes les assurances
    @Override
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    // Méthode pour récupérer une assurance par son ID
    @Override
    public Insurance getInsuranceById(Integer id) {

        return insuranceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));
    }

    // Méthode pour enregistrer une assurance
    @Override
    public Insurance saveInsurance(Insurance insurance) {
        /////////////////MAILING//////////////////////////
        emailService.sendSimpleMailMessage(insurance,insurance.getUser().getEmail());
        //////////////////////////////////////////////////

        return insuranceRepository.save(insurance);
    }



    // Méthode pour mettre à jour une assurance
    @Override
    public Insurance updateInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    // Méthode pour supprimer une assurance
    @Override
    public void deleteInsurance(Integer id) {
        insuranceRepository.deleteById(id);
    }
}
/////////////////MAILING//////////////////////////
//emailService.sendSimpleMailMessage(c.getSurname()+ " "+ c.getName(),c.getEmail(),confirmation.getToken());
